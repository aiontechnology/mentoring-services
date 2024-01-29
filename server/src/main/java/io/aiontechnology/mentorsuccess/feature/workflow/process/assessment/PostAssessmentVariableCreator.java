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

package io.aiontechnology.mentorsuccess.feature.workflow.process.assessment;

import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.feature.workflow.process.variables.RegistrationBaseVariableAddingFunction;
import io.aiontechnology.mentorsuccess.feature.workflow.process.variables.RegistrationTimeoutVariableAddingFunction;
import io.aiontechnology.mentorsuccess.feature.workflow.process.variables.SchoolSessionVariableAddingFunction;
import io.aiontechnology.mentorsuccess.feature.workflow.process.variables.StudentVariableAddingFunction;
import io.aiontechnology.mentorsuccess.feature.workflow.process.variables.TeacherVariableAddingFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostAssessmentVariableCreator {

    // Services
    private final RegistrationBaseVariableAddingFunction registrationBaseVariableAddingFunction;
    private final RegistrationTimeoutVariableAddingFunction registrationTimeoutVariableAddingFunction;
    private final SchoolSessionVariableAddingFunction schoolSessionVariableAddingFunction;
    private final StudentVariableAddingFunction studentVariableAddingFunction;
    private final TeacherVariableAddingFunction teacherVariableAddingFunction;

    public Map<String, Object> create(Optional<Student> student, Optional<SchoolSession> session,
            Optional<SchoolPersonRole> teacher, URI registrationBase, String registrationTimeout) {
        Map<String, Object> variables = new HashMap<>();
        variables.putAll(studentVariableAddingFunction.apply(student));
        variables.putAll(schoolSessionVariableAddingFunction.apply(session));
        variables.putAll(registrationBaseVariableAddingFunction.apply(registrationBase));
        variables.putAll(registrationTimeoutVariableAddingFunction.apply(registrationTimeout));
        variables.putAll(teacherVariableAddingFunction.apply(teacher));
        return variables;
    }

}
