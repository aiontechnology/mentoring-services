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

import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import io.aiontechnology.mentorsuccess.service.StudentRegistrationService;
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.STUDENT_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.TEACHER_ID;

@Service
@RequiredArgsConstructor
public class StartTeacherInfoProcess implements JavaDelegate {

    // Services
    private final StudentRegistrationService studentRegistrationService;

    // Other
    private final TaskUtilities taskUtilities;

    @Override
    public void execute(DelegateExecution execution) {
        String schoolId = taskUtilities.getRequiredVariable(execution, SCHOOL_ID, String.class);
        String studentId = taskUtilities.getRequiredVariable(execution, STUDENT_ID, String.class);
        String teacherId = execution.getVariable(TEACHER_ID, String.class);
        String registrationTimeout = taskUtilities.getRequiredVariable(execution, REGISTRATION_TIMEOUT, String.class);

        InboundInvitation invitation = taskUtilities.getRequiredVariable(execution, INVITATION,
                InboundInvitation.class);

        studentRegistrationService.startStudentInformationProcess(schoolId, studentId, teacherId,
                invitation.getStudentRegistrationUri(), registrationTimeout);
    }

}
