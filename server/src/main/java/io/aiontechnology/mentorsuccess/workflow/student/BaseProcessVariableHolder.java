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
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_EMAIL_TAG;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
public abstract class BaseProcessVariableHolder {

    private final Map<String, Object> processVariables = new HashMap<>();

    protected BaseProcessVariableHolder(School school, String registrationTimeout) {
        put(SCHOOL_ID, school.getRequiredIdString("A school must have an ID"));
        put(SCHOOL_EMAIL_TAG, school.getEmailTag());
        put(REGISTRATION_TIMEOUT, registrationTimeout);
    }

    public Map<String, Object> processVariables() {
        return processVariables;
    }

    protected Object put(String key, Object value) {
        return Optional.ofNullable(value)
                .map(v -> processVariables.put(key, v))
                .orElse(null);
    }

}
