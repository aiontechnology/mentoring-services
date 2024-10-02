/*
 * Copyright 2020-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.mapping.tomodel.student;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.StudentPersonRole;
import io.aiontechnology.mentoring.model.outbound.OutboundPerson;
import io.aiontechnology.mentoring.model.outbound.student.OutboundContact;
import io.aiontechnology.mentoring.resource.PersonResource;
import io.aiontechnology.mentoring.util.PhoneService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link StudentPersonEntityToModelMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class StudentPersonRoleEntityToModelMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        Person person = new Person();
        PersonResource outboundPerson = new PersonResource();

        OneWayMapper<Person, OutboundPerson> personEntityToModelMapper = mock(OneWayMapper.class);
        when(personEntityToModelMapper.map(person)).thenReturn(Optional.of(outboundPerson));

        String label = "Parent";
        StudentPersonRole studentPersonRole = new StudentPersonRole();
        studentPersonRole.setPerson(person);
        studentPersonRole.setLabel(label);

        StudentPersonEntityToModelMapper studentPersonEntityToModelMapper =
                new StudentPersonEntityToModelMapper(new PhoneService());

        // execute the SUT
        Optional<OutboundContact> result = studentPersonEntityToModelMapper.map(studentPersonRole);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get().getFirstName()).isEqualTo(outboundPerson.getFirstName());
        assertThat(result.get().getLastName()).isEqualTo(outboundPerson.getLastName());
        assertThat(result.get().getPhone()).isEqualTo(outboundPerson.getCellPhone());
        assertThat(result.get().getEmail()).isEqualTo(outboundPerson.getEmail());
        assertThat(result.get().getLabel()).isEqualTo(label);
    }

    @Test
    void testNull() throws Exception {
        // setup the fixture
        OneWayMapper<Person, OutboundPerson> personEntityToModelMapper = mock(OneWayMapper.class);
        when(personEntityToModelMapper.map(any())).thenReturn(Optional.empty());

        StudentPersonEntityToModelMapper studentPersonEntityToModelMapper =
                new StudentPersonEntityToModelMapper(new PhoneService());

        // execute the SUT
        Optional<OutboundContact> result = studentPersonEntityToModelMapper.map(null);

        // validation
        assertThat(result).isEmpty();
    }

}
