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

package io.aiontechnology.mentorsuccess.workflow.teacher;

import io.aiontechnology.mentorsuccess.util.UriBuilder;
import io.aiontechnology.mentorsuccess.velocity.TeacherEmailGeneratorSupport;
import io.aiontechnology.mentorsuccess.workflow.EmailGeneratorSupport;
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_BASE;

@Service
@RequiredArgsConstructor
public class InformationEmailGenerationTask extends EmailGeneratorSupport {

    // Other
    private final TaskUtilities taskUtilities;

    @Override
    protected String getBody(DelegateExecution execution) {
        return getGenerationStrategy(execution, TeacherEmailGeneratorSupport.class).render(
                taskUtilities.getTeacherFullName(execution).orElse(""),
                taskUtilities.getStudentFullName(execution).orElse(""),
                taskUtilities.getProgramAdminFullName(execution),
                taskUtilities.getProgramAdminEmail(execution),
                createInformationUri(execution).orElse(""),
                createAssessmentUri(execution).orElse(""));
    }

    @Override
    protected String getFrom(DelegateExecution execution) {
        return DEFAULT_FROM_EMAIL_ADDRESS;
    }

    @Override
    protected String getSubject(DelegateExecution execution) {
        return "His Heart Foundation - MentorSuccess™ Student Info Request";
    }

    @Override
    protected String getTo(DelegateExecution execution) {
        return taskUtilities.getTeacherEmailAddress(execution)
                .orElseThrow(() -> new IllegalStateException("Unable to find teacher email address"));
    }

    private Optional<String> createInformationUri(DelegateExecution execution) {
        String registrationBase = taskUtilities.getRequiredVariable(execution, REGISTRATION_BASE, String.class);
        return taskUtilities.getStudent(execution)
                .map(student -> new UriBuilder(registrationBase)
                        .withPathAddition("schools")
                        .withPathAddition(student.getSchool().getId().toString())
                        .withPathAddition("students")
                        .withPathAddition(student.getId().toString())
                        .withPathAddition("information")
                        .withPathAddition(execution.getProcessInstanceId())
                        .build());
    }

    private Optional<String> createAssessmentUri(DelegateExecution execution) {
        String registrationBase = taskUtilities.getRequiredVariable(execution, REGISTRATION_BASE, String.class);
        return taskUtilities.getStudent(execution)
                .map(student -> new UriBuilder(registrationBase)
                        .withPathAddition("schools")
                        .withPathAddition(student.getSchool().getId().toString())
                        .withPathAddition("students")
                        .withPathAddition(student.getId().toString())
                        .withPathAddition("assessment")
                        .withPathAddition(execution.getProcessInstanceId())
                        .build());
    }

}
