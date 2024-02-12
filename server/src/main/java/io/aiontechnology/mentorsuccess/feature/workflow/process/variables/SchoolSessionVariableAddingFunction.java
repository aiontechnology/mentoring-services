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

package io.aiontechnology.mentorsuccess.feature.workflow.process.variables;

import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.SCHOOL_SESSION_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_EMAIL_TAG;

@Component
@Slf4j
public class SchoolSessionVariableAddingFunction implements Function<Optional<SchoolSession>, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(Optional<SchoolSession> schoolSession) {
        var map = new HashedMap<String, Object>();
        schoolSession.ifPresent(s -> {
                map.put(SCHOOL_SESSION_ID, s.getId().toString());
                map.put(SCHOOL_EMAIL_TAG, s.getSchool().getEmailTag());
        });
        return map;
    }

}
