/*
 * Copyright 2022-2024 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentorsuccess.service;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.api.error.WorkflowException;
import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.entity.StudentSchoolSession;
import io.aiontechnology.mentorsuccess.entity.workflow.StudentRegistration;
import io.aiontechnology.mentorsuccess.feature.workflow.process.ProcessIdToSingleTaskFunction;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudent;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentInformation;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentRegistration;
import io.aiontechnology.mentorsuccess.workflow.FlowableProcessUtilities;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.NEW_STUDENT;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_BASE;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SHOULD_CANCEL;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.STUDENT_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.STUDENT_INFORMATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.TEACHER_ID;

@Service
@RequiredArgsConstructor()
@Slf4j
public class StudentRegistrationService {

    // Mappers
    private final OneWayMapper<InboundStudent, Student> studentModelToEntityMapper;
    private final OneWayMapper<InboundStudent, StudentSchoolSession> studentSessionModelToEntityMapper;
    private final OneWayMapper<InboundStudentRegistration, InboundStudent> studentRegistrationToStudentMapper;

    // Services
    private final FlowableProcessUtilities flowableProcessUtilities;
    private final ProcessIdToSingleTaskFunction flowableTaskUtilities;
    private final RuntimeService runtimeService;
    private final SchoolService schoolService;
    private final StudentService studentService;
    private final TaskService taskService;

    public void cancelRegistration(UUID processId) {
        completeTask(processId, Map.of(
                SHOULD_CANCEL, true
        ));
    }

    @Transactional
    public Optional<Pair<Student, StudentSchoolSession>> createStudent(UUID schoolId, InboundStudent inboundStudent) {
        return schoolService.getSchoolById(schoolId)
                .map(school -> {
                    SchoolSession currentSession = school.getCurrentSession();

                    Student student = studentModelToEntityMapper.map(inboundStudent)
                            .orElseThrow(() -> new NotFoundException("Student was not found"));

                    StudentSchoolSession studentSchoolSession = studentSessionModelToEntityMapper.map(inboundStudent)
                            .map(ss -> ss.setSession(currentSession))
                            .orElseThrow(() -> new IllegalStateException("Mapping of student session failed"));

                    student.addStudentSession(studentSchoolSession);
                    school.addStudent(student);
                    studentService.updateStudent(student);

                    return Pair.of(student, studentSchoolSession);
                });
    }

    public Optional<StudentRegistration> findStudentRegistrationWorkflowById(UUID processId) {
        StudentRegistration studentRegistration = new StudentRegistration();
        TaskQuery query = taskService.createTaskQuery()
                .processInstanceId(processId.toString())
                .includeProcessVariables();
        return query.list().stream()
                .findFirst()
                .map(task -> {
                    studentRegistration.setId(UUID.fromString(task.getProcessInstanceId()));
                    return task;
                })
                .map(TaskInfo::getProcessVariables)
                .map(variables -> {
                    InboundInvitation inboundInvitation = (InboundInvitation) variables.get(INVITATION);
                    studentRegistration.setStudentFirstName(inboundInvitation.getStudentFirstName());
                    studentRegistration.setStudentLastName(inboundInvitation.getStudentLastName());
                    studentRegistration.setParent1FirstName(inboundInvitation.getParent1FirstName());
                    studentRegistration.setParent1LastName(inboundInvitation.getParent1LastName());
                    studentRegistration.setParent1EmailAddress(inboundInvitation.getParent1EmailAddress());
                    return studentRegistration;
                });
    }

    public void processRegistration(UUID schoolId, UUID processId, InboundStudentRegistration studentRegistration) {
        studentRegistrationToStudentMapper.map(studentRegistration)
                .ifPresent(student -> {
                    completeTask(processId, Map.of(
                            REGISTRATION, studentRegistration,
                            NEW_STUDENT, student,
                            SHOULD_CANCEL, false
                    ));
                });

    }

    public void processStudentInformation(UUID schoolId, UUID studentId, UUID processId,
            InboundStudentInformation studentInformation) {
        completeTask(processId, Map.of(
                STUDENT_ID, studentId,
                STUDENT_INFORMATION, studentInformation,
                SHOULD_CANCEL, false
        ));
    }

    /**
     * Start the student information flow unless it is currently running or has run to completion
     *
     * @param schoolId
     * @param studentId
     * @param teacherId
     * @param registrationBase
     * @param registrationTimeout
     */
    @Transactional
    public void startStudentInformationProcess(String schoolId, String studentId, String teacherId,
            String registrationBase, String registrationTimeout) {
        final SchoolSession currentSchoolSession = schoolService.getSchoolById(UUID.fromString(schoolId))
                .map(School::getCurrentSession)
                .orElseThrow();
        studentService.getStudentById(UUID.fromString(studentId))
                .flatMap(student -> student.findCurrentSessionForStudent(currentSchoolSession))
                .filter(currentStudentSchoolSession -> currentStudentSchoolSession.getCompletedInfoFlowId() == null)
                .ifPresentOrElse(currentStudentSchoolSession -> {
                            Map<String, Object> variables = new HashMap<>();
                            variables.put(SCHOOL_ID, schoolId);
                            variables.put(STUDENT_ID, studentId);
                            if (teacherId != null) {
                                variables.put(TEACHER_ID, teacherId);
                            }
                            variables.put(REGISTRATION_BASE, registrationBase);
                            variables.put(REGISTRATION_TIMEOUT, registrationTimeout);

                            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                                    "request-student-info", variables);
                        },
                        () -> {
                            throw new WorkflowException("Student info workflow may not be run twice");
                        }
                );
    }

    private void completeTask(UUID processId, Map<String, Object> variables) {
        TaskQuery query = taskService.createTaskQuery()
                .processInstanceId(processId.toString())
                .includeProcessVariables();
        query.list().stream()
                .findFirst()
                .ifPresent(task -> {
                    runtimeService.setVariables(processId.toString(), variables);
                    taskService.complete(task.getId());
                });
    }

}
