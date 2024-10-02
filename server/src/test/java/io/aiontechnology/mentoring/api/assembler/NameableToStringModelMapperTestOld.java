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
import io.aiontechnology.mentoring.entity.reference.AbstractReference;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class NameableToStringModelMapperTestOld {

    @Test
    void testWithValue() {
        // set up the fixture
        String name = "NAME";
        AbstractReference abstractReference = new AbstractReference() {
            @Override
            public String getName() {
                return name;
            }
        };
        OneWayMapper<AbstractReference, String> mapper = new OneWayMapper<AbstractReference, String>() {
            @Override
            public Optional<String> map(AbstractReference abstractReference) {
                return Optional.of(abstractReference.getName());
            }
        };
        NameableToStringModelMapper<AbstractReference> assembler =
                new NameableToStringModelMapper<>(mapper);

        // execute the SUT
        String result = assembler.toModel(abstractReference);

        // validation
        assertThat(result).isEqualTo(name);
    }

    @Test
    void testWithNull() {
        // set up the fixture
        AbstractReference abstractReference = null;
        OneWayMapper<AbstractReference, String> mapper = new OneWayMapper<AbstractReference, String>() {
            @Override
            public Optional<String> map(AbstractReference abstractReference) {
                return Optional.of(abstractReference.getName());
            }
        };
        NameableToStringModelMapper<AbstractReference> assembler =
                new NameableToStringModelMapper<>(mapper);

        // execute the SUT
        String result = assembler.toModel(abstractReference);

        // validation
        assertThat(result).isNull();
    }

}
