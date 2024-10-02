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

package io.aiontechnology.mentoring.api.mapping.toentity.misc;

import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.service.PersonService;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link UriModelToPersonMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class UriModelToPersonMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        UUID personId = UUID.randomUUID();
        URI personUri = URI.create("http://localhost/api/v1/people/" + personId.toString());

        Person person = new Person();

        PersonService personService = mock(PersonService.class);
        when(personService.findPersonById(personId)).thenReturn(Optional.of(person));

        UriModelToPersonMapper uriModelToPersonMapper = new UriModelToPersonMapper(personService);

        // execute the SUT
        Optional<Person> result = uriModelToPersonMapper.map(personUri);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isSameAs(person);
    }

    @Test
    void testMappingNotFound() throws Exception {
        // setup the fixture
        UUID personId = UUID.randomUUID();
        URI personUri = URI.create("http://localhost/api/v1/people/" + personId.toString());

        PersonService personService = mock(PersonService.class);
        when(personService.findPersonById(personId)).thenReturn(Optional.empty());

        UriModelToPersonMapper uriModelToPersonMapper = new UriModelToPersonMapper(personService);

        // execute the SUT
        Optional<Person> result = uriModelToPersonMapper.map(personUri);

        // validation
        assertThat(result).isEmpty();
    }

    @Test
    void testNull() throws Exception {
        // setup the fixture
        UriModelToPersonMapper uriModelToPersonMapper = new UriModelToPersonMapper(null);

        // execute the SUT
        Optional<Person> result = uriModelToPersonMapper.map(null);

        // validation
        assertThat(result).isEmpty();
    }

}
