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

package io.aiontechnology.mentorsuccess.workflow.student;

import io.aiontechnology.mentorsuccess.entity.Person;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import io.aiontechnology.mentorsuccess.velocity.RegistrationCancellationEmailGenerator;
import io.aiontechnology.mentorsuccess.workflow.EmailGeneratorSupport;
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.PROGRAM_ADMIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationEmailGenerationCancellationTask extends EmailGeneratorSupport {

    private final RegistrationCancellationEmailGenerator emailGenerator;

    private final TaskUtilities taskUtilities;

    @Override
    protected String getBody(DelegateExecution execution) {
        Person programAdmin = taskUtilities.getRequiredVariable(execution, PROGRAM_ADMIN, Person.class);
        InboundInvitation invitation = taskUtilities.getRequiredVariable(execution, INVITATION,
                InboundInvitation.class);
        return emailGenerator.render(programAdmin.getFirstName(), invitation);
    }

    @Override
    protected String getFrom(DelegateExecution execution) {
        return "do-not-reply@mentorsuccesskids.com";
    }

    @Override
    protected String getSubject(DelegateExecution execution) {
        InboundInvitation invitation = taskUtilities.getRequiredVariable(execution, INVITATION,
                InboundInvitation.class);
        return "Registration cancelled for " + invitation.getStudentFullName();
    }

    @Override
    protected String getTo(DelegateExecution execution) {
        return taskUtilities.getRequiredVariable(execution, PROGRAM_ADMIN, Person.class).getEmail();
    }

}
