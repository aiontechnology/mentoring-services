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

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.mentoring.entity.StudentPersonRole;
import io.aiontechnology.mentoring.util.PhoneService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests of {@link StudentPersonModelToEntityUpdateMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class StudentPersonRoleModelToEntityUpdateMapperTest {

    @Test
    void testMapping() throws Exception {
//        // setup the fixture
//        URI personUri = URI.create("http://localhost/api/v1/people/1234");
//        Person person = new Person();
//
//        OneWayMapper<URI, Person> uriModelToEntityMapper = mock(OneWayMapper.class);
//        when(uriModelToEntityMapper.map(personUri)).thenReturn(Optional.of(person));
//
//        InboundEmergencyContactModel inboundEmergencyContactModel = new InboundEmergencyContactModel(
//                personUri, PRINCIPAL);
//
//        StudentPerson studentPerson = new StudentPerson();
//
//        StudentPersonModelToEntityUpdateMapper studentPersonModelToEntityUpdateMapper =
//                new StudentPersonModelToEntityUpdateMapper(uriModelToEntityMapper);
//
//        // execute the SUT
//        Optional<StudentPerson> result = studentPersonModelToEntityUpdateMapper.map(
//                inboundEmergencyContactModel, studentPerson);
//
//        // validation
//        assertThat(result).isNotEmpty();
//        assertThat(result.get().getPerson()).isSameAs(person);
//        assertThat(result.get().getPersonType()).isEqualTo(PRINCIPAL);
    }

    @Test
    void testNull() throws Exception {
        // setup the fixture
        StudentPersonRole studentPersonRole = new StudentPersonRole();

        StudentPersonModelToEntityUpdateMapper studentPersonModelToEntityUpdateMapper =
                new StudentPersonModelToEntityUpdateMapper(null, new PhoneService());

        // execute the SUT
        Optional<StudentPersonRole> result = studentPersonModelToEntityUpdateMapper.map(null, studentPersonRole);

        // validation
        assertThat(result).isEmpty();
    }

}
