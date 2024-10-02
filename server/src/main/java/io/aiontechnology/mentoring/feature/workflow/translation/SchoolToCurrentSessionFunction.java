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

package io.aiontechnology.mentoring.feature.workflow.translation;

import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * This class is a function that maps a given school ID to its current session. It implements the Function interface
 * with the UUID as the input type and SchoolSession as the output type.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SchoolToCurrentSessionFunction implements Function<Optional<School>, Optional<SchoolSession>> {

    @Override
    public Optional<SchoolSession> apply(Optional<School> school) {
        return school.map(School::getCurrentSession);
    }

}
