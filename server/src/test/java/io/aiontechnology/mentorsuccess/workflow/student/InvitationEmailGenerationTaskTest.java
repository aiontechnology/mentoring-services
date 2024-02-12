/*
 * Copyright 2024 Aion Technology LLC
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
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.aiontechnology.mentorsuccess.workflow.EmailGeneratorSupport.DEFAULT_FROM_EMAIL_ADDRESS;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvitationEmailGenerationTaskTest {

    @Test
    void testFrom() {
        // set up the fixture
        var task = createTask();
        var delegateExecution = mock(DelegateExecution.class);

        // execute the SUT
        var result = task.getFrom(delegateExecution);

        // validation
        assertThat(result).isEqualTo(DEFAULT_FROM_EMAIL_ADDRESS);
    }

    @Test
    void testSubject() {
        // set up the fixture
        var task = createTask();
        var delegateExecution = mock(DelegateExecution.class);

        // execute the SUT
        var result = task.getSubject(delegateExecution);

        // validation
        assertThat(result).isEqualTo("His Heart Foundation - MentorSuccess™ Student Registration");
    }

    @Test
    void testTo() {
        // set up the fixture
        var task = createTask();
        var delegateExecution = mock(DelegateExecution.class);

        // execute the SUT
        var result = task.getTo(delegateExecution);

        // validation
        assertThat(result).isEqualTo("FIRST NAME <test@example.com>");
    }

    private InvitationEmailGenerationTask createTask() {
        var invitation = InboundInvitation.builder()
                .withParent1FirstName("FIRST")
                .withParent1LastName("NAME")
                .withParent1EmailAddress("test@example.com")
                .build();

        var taskUtilities = mock(TaskUtilities.class);
        when(taskUtilities.getRequiredVariable(any(), eq(INVITATION), eq(InboundInvitation.class)))
                .thenReturn(invitation);
        when(taskUtilities.getSchoolEmailTag(any())).thenReturn(Optional.of("TAG"));

        return new InvitationEmailGenerationTask(taskUtilities);
    }

}
