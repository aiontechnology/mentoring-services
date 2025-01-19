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

package io.aiontechnology.mentoring.workflow.student;

import io.aiontechnology.mentoring.api.mapping.toentity.misc.UriModelToRoleMapper;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentRegistration;
import io.aiontechnology.mentoring.velocity.RegistrationCompleteEmailGenerator;
import io.aiontechnology.mentoring.workflow.ProgramAdministratorEmailGeneratorSupport;
import io.aiontechnology.mentoring.workflow.TaskUtilities;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.REGISTRATION;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.TEACHER_ID;

@Slf4j
@Service
public class RegistrationCompleteEmailGenerationTask extends ProgramAdministratorEmailGeneratorSupport {

    private final UriModelToRoleMapper uriToRoleMapper;

    public RegistrationCompleteEmailGenerationTask(TaskUtilities taskUtilities, UriModelToRoleMapper uriToRoleMapper) {
        super(taskUtilities);
        this.uriToRoleMapper = uriToRoleMapper;
    }

    @Override
    protected String getBody(DelegateExecution execution) {
        String programAdminName = getTaskUtilities().getProgramAdminFullName(execution);
        InboundStudentRegistration studentRegistration = getTaskUtilities().getRequiredVariable(execution, REGISTRATION,
                InboundStudentRegistration.class);
        Optional<Person> teacher = getTeacher(studentRegistration).map(SchoolPersonRole::getPerson);
        return getGenerationStrategy(execution, RegistrationCompleteEmailGenerator.class)
                .render(programAdminName, teacher.map(Person::getFullName).orElse(""), studentRegistration);
    }

    @Override
    protected String getFrom(DelegateExecution execution) {
        return DEFAULT_FROM_EMAIL_ADDRESS;
    }

    @Override
    protected String getSubject(DelegateExecution execution) {
        InboundStudentRegistration studentRegistration = getTaskUtilities().getRequiredVariable(execution, REGISTRATION,
                InboundStudentRegistration.class);
        return "Completed Registration: " + studentRegistration.getStudentFullName();
    }

    @Override
    protected void setAdditionalVariables(DelegateExecution execution) {
        InboundStudentRegistration studentRegistration = getTaskUtilities().getRequiredVariable(execution, REGISTRATION,
                InboundStudentRegistration.class);
        Optional<SchoolPersonRole> teacher = getTeacher(studentRegistration);
        execution.setVariable(TEACHER_ID, teacher
                .map(SchoolPersonRole::getId)
                .map(UUID::toString)
                .orElse(null));
    }

    private Optional<SchoolPersonRole> getTeacher(InboundStudentRegistration studentRegistration) {
        return Optional.ofNullable(studentRegistration.getTeacher())
                .flatMap(uriToRoleMapper::map);
    }

}
