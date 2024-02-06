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

import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import lombok.RequiredArgsConstructor;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT_VALUE;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

public class StudentRegistrationProcessVariableHolder extends BaseProcessVariableHolder {

    private StudentRegistrationProcessVariableHolder(School school, InboundInvitation invitation,
            String registrationTimeout) {
        super(school, registrationTimeout);
        put(INVITATION, invitation);
    }

    public static RegisterStudentProcessVariableHolderBuilder builder(School school,
            InboundInvitation inboundInvitation) {
        return new RegisterStudentProcessVariableHolderBuilder(school, inboundInvitation);
    }

    @RequiredArgsConstructor(access = PRIVATE)
    public static class RegisterStudentProcessVariableHolderBuilder {

        private final School school;
        private final InboundInvitation invitation;
        private String registrationTimeout = REGISTRATION_TIMEOUT_VALUE;

        public StudentRegistrationProcessVariableHolder build() {
            return new StudentRegistrationProcessVariableHolder(
                    requireNonNull(school, "A school is required"),
                    requireNonNull(invitation, "An invitation is required"),
                    registrationTimeout);
        }

        public RegisterStudentProcessVariableHolderBuilder withRegistrationTimeout(String registrationTimeout) {
            this.registrationTimeout = requireNonNull(registrationTimeout, "A registration timeout is required");
            return this;
        }

    }

}
