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

package io.aiontechnology.mentoring.workflow.student;

import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import io.aiontechnology.mentoring.service.StudentRegistrationService;
import io.aiontechnology.mentoring.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.NEW_STUDENT;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.STUDENT_ID;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.TEACHER_ID;

@Service
@RequiredArgsConstructor
public class StudentRegistrationStoreStudentTask implements JavaDelegate {

    private final StudentRegistrationService studentRegistrationService;

    private final TaskUtilities taskUtilities;

    @Override
    public void execute(DelegateExecution execution) {
        School school = taskUtilities.getSchool(execution).orElseThrow();
        InboundStudent student = taskUtilities.getRequiredVariable(execution, NEW_STUDENT, InboundStudent.class);

        studentRegistrationService.createStudent(school.getId(), student)
                .ifPresent(pair -> {
                    Person teacher = pair.getRight().getTeacher() != null
                            ? pair.getRight().getTeacher().getPerson()
                            : null;
                    if (teacher != null) {
                        execution.setVariable(TEACHER_ID, teacher.getId().toString());
                    }
                    execution.setVariable(STUDENT_ID, pair.getLeft().getId().toString());
                });
    }

}
