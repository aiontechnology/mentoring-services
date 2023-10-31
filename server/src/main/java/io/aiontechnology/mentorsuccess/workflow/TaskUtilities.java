/*
 * Copyright 2023 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.workflow;

import io.aiontechnology.mentorsuccess.entity.Person;
import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.model.enumeration.RoleType;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentAssessment;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentInformation;
import io.aiontechnology.mentorsuccess.service.SchoolService;
import io.aiontechnology.mentorsuccess.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.STUDENT_ASSESSMENT;
import static io.aiontechnology.mentorsuccess.model.enumeration.RoleType.PROGRAM_ADMIN;
import static io.aiontechnology.mentorsuccess.model.enumeration.RoleType.TEACHER;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.STUDENT_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.STUDENT_INFORMATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.TEACHER_ID;

@Service
@RequiredArgsConstructor
public class TaskUtilities {

    // Services
    private final SchoolService schoolService;
    private final StudentService studentService;

    public Optional<InboundStudentAssessment> getInboundStudentAssessment(DelegateExecution execution) {
        return Optional.ofNullable(execution.getVariable(STUDENT_ASSESSMENT, InboundStudentAssessment.class));
    }

    public Optional<InboundStudentInformation> getInboundStudentInformation(DelegateExecution execution) {
        return Optional.ofNullable(execution.getVariable(STUDENT_INFORMATION, InboundStudentInformation.class));
    }

    public String getProgramAdminEmail(DelegateExecution execution) {
        return getProgramAdmin(execution)
                .map(Person::getEmail)
                .orElseThrow(() -> new IllegalStateException("Unable to find program admin email address"));
    }

    public String getProgramAdminFullName(DelegateExecution execution) {
        return getProgramAdmin(execution)
                .map(Person::getFullName)
                .orElseThrow(() -> new IllegalStateException("Unable to find program admin name"));
    }

    public <T> T getRequiredVariable(DelegateExecution execution, String variableName, Class<T> clazz) {
        T value = execution.getVariable(variableName, clazz);
        if (value == null) {
            throw new IllegalStateException("Unable to find value for required variable: " + variableName);
        }
        return value;
    }

    /**
     * Retrieve the {@link School} based on the SCHOOL_ID that is contained in a process variable.
     *
     * @param execution The execution object from which process variables are retrieved.
     * @return The {@link School} if it could be located.
     */
    public Optional<School> getSchool(DelegateExecution execution) {
        UUID schoolId = UUID.fromString(getRequiredVariable(execution, SCHOOL_ID, String.class));
        return schoolService.getSchoolById(schoolId);
    }

    public Optional<Student> getStudent(DelegateExecution execution) {
        return Optional.ofNullable(execution.getVariable(STUDENT_ID, String.class))
                .map(UUID::fromString)
                .flatMap(studentService::getStudentById);
    }

    public Optional<String> getStudentFullName(DelegateExecution execution) {
        return getStudent(execution)
                .map(Student::getFullName);
    }

    public Optional<SchoolPersonRole> getTeacher(DelegateExecution execution) {
        return Optional.ofNullable(execution.getVariable(TEACHER_ID, String.class))
                .map(UUID::fromString)
                .flatMap(teacherId -> getSchoolRolesInRole(execution, TEACHER).stream()
                        .filter(person -> person.getId().equals(teacherId))
                        .findFirst());
    }

    public Optional<String> getTeacherEmailAddress(DelegateExecution execution) {
        return getTeacher(execution)
                .map(SchoolPersonRole::getPerson)
                .map(Person::getEmail);
    }

    public Optional<String> getTeacherFullName(DelegateExecution execution) {
        return getTeacher(execution)
                .map(SchoolPersonRole::getPerson)
                .map(Person::getFullName);
    }

    private Collection<Person> getPeopleInRole(DelegateExecution execution, RoleType type) {
        return getSchoolRolesInRole(execution, type).stream()
                .map(SchoolPersonRole::getPerson)
                .collect(Collectors.toList());
    }

    /**
     * Get the program administrator for the school that is specified by the "schoolId" execution variable.
     *
     * @param execution The context from which the schoolId variable should be found.
     * @return The school's program administrator.
     */
    private Optional<Person> getProgramAdmin(DelegateExecution execution) {
        return getPeopleInRole(execution, PROGRAM_ADMIN)
                .stream()
                .findFirst();
    }

    private Collection<SchoolPersonRole> getSchoolRolesInRole(DelegateExecution execution, RoleType type) {
        Collection<SchoolPersonRole> roles = getSchool(execution)
                .map(School::getRoles)
                .orElse(Collections.emptyList());
        return roles.stream()
                .filter(role -> role.getType().equals(type))
                .collect(Collectors.toList());
    }

}
