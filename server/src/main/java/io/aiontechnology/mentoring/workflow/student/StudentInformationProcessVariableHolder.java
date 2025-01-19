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

package io.aiontechnology.mentoring.workflow.student;

import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.Student;
import lombok.RequiredArgsConstructor;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.REGISTRATION_BASE;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT_VALUE;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.STUDENT_ID;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.TEACHER_ID;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

public class StudentInformationProcessVariableHolder extends BaseProcessVariableHolder {

    private StudentInformationProcessVariableHolder(School school, Student student, SchoolPersonRole teacher,
            String registrationBase, String registrationTimeout) {
        super(school, registrationTimeout);
        put(STUDENT_ID, student.getRequiredIdString("A student must have an ID"));
        put(REGISTRATION_BASE, registrationBase);
        if (teacher != null) {
            put(TEACHER_ID, teacher.getRequiredIdString("A teacher must have an ID"));
        }
    }

    public static StudentInformationProcessVariableHolderBuilder builder(School school, Student student,
            String registrationBase) {
        return new StudentInformationProcessVariableHolderBuilder(school, student, registrationBase);
    }

    @RequiredArgsConstructor(access = PRIVATE)
    public static class StudentInformationProcessVariableHolderBuilder {

        private final School school;
        private final Student student;
        private final String registrationBase;
        private String registrationTimeout = REGISTRATION_TIMEOUT_VALUE;
        private SchoolPersonRole teacher;

        public StudentInformationProcessVariableHolder build() {
            return new StudentInformationProcessVariableHolder(
                    requireNonNull(school, "A school is required"),
                    requireNonNull(student, "A student is required"),
                    teacher,
                    registrationBase,
                    registrationTimeout);
        }

        public StudentInformationProcessVariableHolderBuilder withRegistrationTimeout(String registrationTimeout) {
            this.registrationTimeout = requireNonNull(registrationTimeout, "A registration timeout is required");
            return this;
        }

        public StudentInformationProcessVariableHolderBuilder withTeacher(SchoolPersonRole teacher) {
            this.teacher = requireNonNull(teacher, "A teacher is required");
            return this;
        }

    }

}
