/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.assembler;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.reference.Nameable;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Map the name of an {@link Nameable} to a String.
 *
 * @author Whitney Hunter
 * @since 1.8.0
 */
@RequiredArgsConstructor
public class NameableToStringModelMapper<T extends Nameable> {

    private final OneWayMapper<T, String> mapper;

    public String toModel(T item) {
        return Optional.ofNullable(item)
                .flatMap(mapper::map)
                .orElse(null);
    }

}
