/*
 * Copyright 2021-2023 Aion Technology LLC
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

import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import io.aiontechnology.mentorsuccess.util.UriBuilder;
import io.aiontechnology.mentorsuccess.velocity.StudentInvitationEmailGenerator;
import io.aiontechnology.mentorsuccess.workflow.EmailGeneratorSupport;
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;

/**
 * {@link JavaDelegate} that will set the email property that is needed to send an email.
 * <br/>Inbound properties
 * <table>
 *   <tr>
 *     <th>name</th>
 *     <th>class</th>
 *   </tr>
 *   <tr>
 *     <td>school</td>
 *     <td>School</td>
 *   </tr>
 *   <tr>
 *     <td>invitation</td>
 *     <td>InboundInvitation</td>
 *   </tr>
 *   <tr>
 *      <td>programAdmin</td>
 *      <td>Person</td>
 *   </tr>
 * </table>
 *
 * @author Whitney Hunter
 * @since 1.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationEmailGenerationTask extends EmailGeneratorSupport {

    private final TaskUtilities taskUtilities;

    @Override
    protected String getBody(DelegateExecution execution) {
        School school = taskUtilities.getSchool(execution).orElseThrow();
        InboundInvitation invitation = taskUtilities.getRequiredVariable(execution, INVITATION,
                InboundInvitation.class);
        String programAdminName = taskUtilities.getProgramAdminFullName(execution);
        String programAdminEmail = taskUtilities.getProgramAdminEmail(execution);
        String programAdminPhone = taskUtilities.getProgramAdminEmail(execution);
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
        return taskUtilities.getRequiredVariable(execution, INVITATION, InboundInvitation.class).getParent1EmailAddress();
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
