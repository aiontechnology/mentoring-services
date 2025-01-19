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

package io.aiontechnology.mentoring.feature.workflow.process.variables;

import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static io.aiontechnology.mentoring.feature.workflow.WorkflowKeys.TEACHER_ID;

@Component
@Slf4j
public class TeacherVariableAddingFunction implements Function<Optional<SchoolPersonRole>, Map<String, Object>> {

    @Override
    public Map<String, Object> apply(Optional<SchoolPersonRole> teacher) {
        var map = new HashedMap<String, Object>();
        teacher.ifPresent(t -> map.put(TEACHER_ID, t.getId().toString()));
        return map;
    }

}
