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

package io.aiontechnology.mentoring.workflow.teacher;

import io.aiontechnology.mentoring.velocity.StudentInfoTimeoutEmailGenerator;
import io.aiontechnology.mentoring.workflow.ProgramAdministratorEmailGeneratorSupport;
import io.aiontechnology.mentoring.workflow.TaskUtilities;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

@Service
public class StudentInfoEmailGenerationTimeoutTask extends ProgramAdministratorEmailGeneratorSupport {

    public StudentInfoEmailGenerationTimeoutTask(TaskUtilities taskUtilities) {
        super(taskUtilities);
    }

    @Override
    protected String getBody(DelegateExecution execution) {
        String programAdminName = getTaskUtilities().getProgramAdminFullName(execution);
        String teacherName = getTaskUtilities().getTeacherFullName(execution).orElseThrow();
        String studentName = getTaskUtilities().getStudentFullName(execution).orElseThrow();
        return getGenerationStrategy(execution, StudentInfoTimeoutEmailGenerator.class)
                .render(programAdminName, teacherName, studentName);
    }

    @Override
    protected String getFrom(DelegateExecution execution) {
        return DEFAULT_FROM_EMAIL_ADDRESS;
    }

    @Override
    protected String getSubject(DelegateExecution execution) {
        String studentName = getTaskUtilities().getStudentFullName(execution).orElse("");
        return "Student information request timed out for " + studentName;
    }

}
