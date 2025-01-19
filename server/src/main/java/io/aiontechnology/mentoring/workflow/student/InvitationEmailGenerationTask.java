/*
 * Copyright 2021-2024 Aion Technology LLC
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

import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.inbound.InboundInvitation;
import io.aiontechnology.mentoring.util.EmailAddress;
import io.aiontechnology.mentoring.util.UriBuilder;
import io.aiontechnology.mentoring.velocity.StudentInvitationEmailGenerator;
import io.aiontechnology.mentoring.workflow.EmailGeneratorSupport;
import io.aiontechnology.mentoring.workflow.TaskUtilities;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.INVITATION;

@Slf4j
@Service
public class InvitationEmailGenerationTask extends EmailGeneratorSupport {

    public InvitationEmailGenerationTask(TaskUtilities taskUtilities) {
        super(taskUtilities);
    }

    @Override
    protected String getBody(DelegateExecution execution) {
        var school = getTaskUtilities().getSchool(execution).orElseThrow();
        var invitation = getTaskUtilities().getRequiredVariable(execution, INVITATION, InboundInvitation.class);
        var programAdminName = getTaskUtilities().getProgramAdminFullName(execution);
        var programAdminEmail = getTaskUtilities().getProgramAdminEmail(execution);
        var programAdminPhone = getTaskUtilities().getProgramAdminPhoneNumber(execution);
        String registrationUri = createRegistrationUri(execution, school, invitation);
        return getGenerationStrategy(execution, StudentInvitationEmailGenerator.class)
                .render(invitation.getParent1FirstName(), school.getName(), programAdminName, programAdminEmail,
                        programAdminPhone, registrationUri);
    }

    @Override
    protected String getFrom(DelegateExecution execution) {
        return DEFAULT_FROM_EMAIL_ADDRESS;
    }

    @Override
    protected String getSubject(DelegateExecution execution) {
        return "His Heart Foundation - MentorSuccess™ Student Registration";
    }

    @Override
    protected String getTo(DelegateExecution execution) {
        var parent = getTaskUtilities().getRequiredVariable(execution, INVITATION, InboundInvitation.class);
        return EmailAddress
                .builder(parent.getParent1EmailAddress())
                .withName(parent.getParent1FullName())
                .build()
                .toString();
    }

    private String createRegistrationUri(DelegateExecution execution, School school, InboundInvitation invitation) {
        return new UriBuilder(invitation.getStudentRegistrationUri())
                .withPathAddition("schools")
                .withPathAddition(school.getId().toString())
                .withPathAddition("registrations")
                .withPathAddition(execution.getProcessInstanceId())
                .build();
    }

}
