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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.REGISTRATION_BASE;

/**
 * A function that registers the provided registration base URI into a map and returns that map.
 */
@Component
@Slf4j
public class RegistrationBaseVariableAddingFunction implements Function<URI, Map<String, Object>> {

    /**
     * Register the provided registration base URI into a map and return that map.
     *
     * @param registrationBase The registration base to register.
     * @return The map that contains the registered Base URI.
     */
    @Override
    public Map<String, Object> apply(URI registrationBase) {
        var map = new HashedMap<String, Object>();
        map.put(REGISTRATION_BASE, registrationBase.toString());
        return map;
    }

}
