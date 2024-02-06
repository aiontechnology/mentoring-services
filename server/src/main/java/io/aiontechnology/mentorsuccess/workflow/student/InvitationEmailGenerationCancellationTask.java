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

package io.aiontechnology.mentorsuccess.workflow.student;

import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import io.aiontechnology.mentorsuccess.velocity.RegistrationCancellationEmailGenerator;
import io.aiontechnology.mentorsuccess.workflow.ProgramAdministratorEmailGeneratorSupport;
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;

@Slf4j
@Service
public class InvitationEmailGenerationCancellationTask extends ProgramAdministratorEmailGeneratorSupport {

    public InvitationEmailGenerationCancellationTask(TaskUtilities taskUtilities) {
        super(taskUtilities);
    }

    @Override
    protected String getBody(DelegateExecution execution) {
        String programAdminName = getTaskUtilities().getProgramAdminFullName(execution);
        InboundInvitation invitation = getTaskUtilities().getRequiredVariable(execution, INVITATION,
                InboundInvitation.class);
        return getGenerationStrategy(execution, RegistrationCancellationEmailGenerator.class)
                .render(programAdminName, invitation);
    }

    @Override
    protected String getFrom(DelegateExecution execution) {
        return DEFAULT_FROM_EMAIL_ADDRESS;
    }

    @Override
    protected String getSubject(DelegateExecution execution) {
        InboundInvitation invitation = getTaskUtilities().getRequiredVariable(execution, INVITATION,
                InboundInvitation.class);
        return "Registration cancelled for " + invitation.getStudentFullName();
    }

}
