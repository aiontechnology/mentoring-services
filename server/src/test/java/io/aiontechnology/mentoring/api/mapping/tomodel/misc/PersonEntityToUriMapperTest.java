/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentoring.api.mapping.tomodel.misc;

import io.aiontechnology.mentoring.entity.Person;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PersonEntityToUriMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class PersonEntityToUriMapperTest {

    @Test
    void testMapWithResult() throws Exception {
        // setup the fixture
        Person person = new Person();
        UUID id = UUID.randomUUID();
        person.setId(id);

        PersonEntityToUriMapper personEntityToUriMapper = new PersonEntityToUriMapper();

        // execute the SUT
        Optional<URI> result = personEntityToUriMapper.map(person);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get().toString()).isEqualTo("/api/v1/people/" + id.toString());
    }

}
