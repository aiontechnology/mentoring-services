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

package io.aiontechnology.mentoring.api.mapping.toentity.person;

import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.model.inbound.InboundPerson;
import io.aiontechnology.mentoring.util.PhoneService;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PersonModelToEntityUpdateMapper}.
 *
 * @author Whitney Hunter
 * @since
 */
public class PersonModelToEntityUpdateMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        UUID id = UUID.randomUUID();
        String firstName = "FIRST_NAME";
        String lastName = "LAST_NAME";
        String workPhone = "WORK_PHONE";
        String cellPhone = "CELL_PHONE";
        String email = "EMAIL";

        InboundPerson inboundPerson = InboundPerson.builder()
                .withId(id)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withWorkPhone(workPhone)
                .withCellPhone(cellPhone)
                .withEmail(email)
                .build();

        Person person = new Person();

        PersonModelToEntityUpdateMapper personModelToEntityUpdateMapper = new PersonModelToEntityUpdateMapper(new PhoneService());

        // execute the SUT
        Optional<Person> result = personModelToEntityUpdateMapper.map(inboundPerson, person);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isSameAs(person);
        assertThat(result.get().getFirstName()).isEqualTo(firstName);
        assertThat(result.get().getLastName()).isEqualTo(lastName);
        assertThat(result.get().getWorkPhone()).isEqualTo(workPhone);
        assertThat(result.get().getCellPhone()).isEqualTo(cellPhone);
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    void testNull() throws Exception {
        // setup the fixture
        Person person = new Person();

        PersonModelToEntityUpdateMapper personModelToEntityUpdateMapper = new PersonModelToEntityUpdateMapper(new PhoneService());

        // execute the SUT
        Optional<Person> result = personModelToEntityUpdateMapper.map(null, person);

        // validation
        assertThat(result).isEmpty();
    }

}
