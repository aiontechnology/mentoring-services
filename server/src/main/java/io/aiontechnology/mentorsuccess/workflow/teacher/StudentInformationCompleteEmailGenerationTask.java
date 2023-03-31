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

import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentInformation;
import io.aiontechnology.mentorsuccess.velocity.TeacherInvitationCompleteEmailGenerator;
import io.aiontechnology.mentorsuccess.workflow.EmailGeneratorSupport;
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SA_EMAIL_ADDRESS;

@Service
@RequiredArgsConstructor
public class StudentInformationCompleteEmailGenerationTask extends EmailGeneratorSupport {

    private final TeacherInvitationCompleteEmailGenerator emailGenerator;

    private final TaskUtilities taskUtilities;

    @Override
    protected String getBody(DelegateExecution execution) {
        String programAdminName = taskUtilities.getProgramAdminFullName(execution);
        String studentName = taskUtilities.getStudentFullName(execution).orElse("");
        InboundStudentInformation studentInformation =
                taskUtilities.getInboundStudentInformation(execution).orElseThrow();
        return emailGenerator.render(programAdminName, studentName, studentInformation);
    }

    @Override
    protected String getFrom(DelegateExecution execution) {
        return DEFAULT_FROM_EMAIL_ADDRESS;
    }

    @Override
    protected String getSubject(DelegateExecution execution) {
        return "Student Information Received: " + taskUtilities.getStudentFullName(execution).orElse("");
    }

    @Override
    protected String getTo(DelegateExecution execution) {
        return String.join(", ", Arrays.asList(SA_EMAIL_ADDRESS, taskUtilities.getProgramAdminEmail(execution)));
    }

}
