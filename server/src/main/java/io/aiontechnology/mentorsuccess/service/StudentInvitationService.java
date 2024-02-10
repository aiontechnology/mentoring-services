/*
 * Copyright 2022-2023 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.service;

import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT_VALUE;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_ID;

@Service
@RequiredArgsConstructor()
@Slf4j
public class StudentInvitationService {

    // Services
    private final RuntimeService runtimeService;

    public void invite(InboundInvitation invitation, School school) {
        runtimeService.startProcessInstanceByKey("register-student", createProcessVariables(invitation, school));
    }

    private Map<String, Object> createProcessVariables(InboundInvitation invitation, School school) {
        return Map.of(
                SCHOOL_ID, school.getId().toString(),
                INVITATION, invitation,
                REGISTRATION_TIMEOUT, REGISTRATION_TIMEOUT_VALUE
        );
    }

}
