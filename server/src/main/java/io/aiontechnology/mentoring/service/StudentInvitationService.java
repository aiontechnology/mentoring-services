/*
 * Copyright 2022-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.service;

import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.inbound.InboundInvitation;
import io.aiontechnology.mentoring.workflow.student.StudentRegistrationProcessVariableHolder;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Service;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.STUDENT_REGISTRATION_PROCESS;

@Service
@RequiredArgsConstructor()
public class StudentInvitationService {

    // Services
    private final RuntimeService runtimeService;

    public void invite(InboundInvitation invitation, School school) {
        var processVariables = StudentRegistrationProcessVariableHolder.builder(school, invitation)
                .build()
                .processVariables();
        runtimeService.startProcessInstanceByKey(STUDENT_REGISTRATION_PROCESS, processVariables);
    }

}
