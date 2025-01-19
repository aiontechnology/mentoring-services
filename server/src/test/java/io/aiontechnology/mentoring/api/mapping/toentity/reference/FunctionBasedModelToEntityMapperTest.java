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

package io.aiontechnology.mentoring.api.mapping.toentity.reference;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class FunctionBasedModelToEntityMapperTest {

    @Test
    void testWithValue() {
        // set up the fixture
        String value = "VALUE";
        Function<String, Optional<String>> getter = s -> Optional.of(s);
        FunctionBasedModelToEntityMapper<String> mapper = new FunctionBasedModelToEntityMapper<>(getter);

        // execute the SUT
        Optional<String> result = mapper.map(value);

        // validation
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(value);
    }

    @Test
    void testWithoutValue() {
        // set up the fixture
        String value = "VALUE";
        Function<String, Optional<String>> getter = s -> Optional.empty();
        FunctionBasedModelToEntityMapper<String> mapper = new FunctionBasedModelToEntityMapper<>(getter);

        // execute the SUT
        Optional<String> result = mapper.map(value);

        // validation
        assertThat(result.isEmpty()).isTrue();
    }

}
