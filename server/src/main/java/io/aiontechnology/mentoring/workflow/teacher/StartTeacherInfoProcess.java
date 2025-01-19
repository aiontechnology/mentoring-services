/*
 * Copyright 2023-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.workflow.teacher;

import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.inbound.InboundInvitation;
import io.aiontechnology.mentoring.service.SchoolService;
import io.aiontechnology.mentoring.service.StudentRegistrationService;
import io.aiontechnology.mentoring.service.StudentService;
import io.aiontechnology.mentoring.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.INVITATION;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.STUDENT_ID;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.TEACHER_ID;

@Service
@RequiredArgsConstructor
public class StartTeacherInfoProcess implements JavaDelegate {

    // Flowable
    private final RuntimeService runtimeService;

    // Services
    private final SchoolService schoolService;
    private final StudentRegistrationService studentRegistrationService;
    private final StudentService studentService;

    // Other
    private final TaskUtilities taskUtilities;

    @Override
    public void execute(DelegateExecution execution) {
        String schoolId = taskUtilities.getRequiredVariable(execution, SCHOOL_ID, String.class);
        String studentId = taskUtilities.getRequiredVariable(execution, STUDENT_ID, String.class);
        String teacherId = execution.getVariable(TEACHER_ID, String.class);
        String registrationTimeout = taskUtilities.getRequiredVariable(execution, REGISTRATION_TIMEOUT, String.class);

        InboundInvitation invitation = taskUtilities.getRequiredVariable(execution, INVITATION,
                InboundInvitation.class);

        var school = schoolService.getSchoolById(UUID.fromString(schoolId));
        var currentSession = school
                .map(School::getCurrentSession);
        var student = currentSession
                .flatMap(session -> studentService.getStudentById(UUID.fromString(studentId), session));
        studentRegistrationService.startStudentInformationProcess(
                school.orElseThrow(),
                student.orElseThrow(),
                invitation.getStudentRegistrationUri(),
                registrationTimeout);
    }

}
