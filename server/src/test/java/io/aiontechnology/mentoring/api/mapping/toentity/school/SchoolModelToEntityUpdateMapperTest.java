/*
 * Copyright 2020-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.mapping.toentity.school;

import io.aiontechnology.mentoring.api.mapping.toentity.misc.AddressModelToEntityUpdateMapper;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.inbound.InboundAddress;
import io.aiontechnology.mentoring.model.inbound.InboundSchool;
import io.aiontechnology.mentoring.util.PhoneService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SchoolModelToEntityUpdateMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class SchoolModelToEntityUpdateMapperTest {

    @Test
    void testMapping() throws Exception {
        // set up the fixture
        String street1 = "STREET1";
        String street2 = "STREET2";
        String city = "CITY";
        String state = "STATE";
        String zip = "ZIP";

        InboundAddress inboundAddress = InboundAddress.builder()
                .withStreet1(street1)
                .withStreet2(street2)
                .withCity(city)
                .withState(state)
                .withZip(zip)
                .build();

        String name = "NAME";
        String phone = "123-456-7890";
        String district = "DISTRICT";
        boolean isPrivate = true;
        String emailTag = "EMAIL_TAG";

        InboundSchool inboundSchool = InboundSchool.builder()
                .withName(name)
                .withAddress(inboundAddress)
                .withPhone(phone)
                .withDistrict(district)
                .withIsPrivate(isPrivate)
                .withEmailTag(emailTag)
                .build();

        School school = new School();

        var phoneService = new PhoneService();

        SchoolModelToEntityUpdateMapper schoolModelToEntityUpdateMapper = new SchoolModelToEntityUpdateMapper(
                new AddressModelToEntityUpdateMapper(), phoneService);

        // execute the SUT
        Optional<School> result = schoolModelToEntityUpdateMapper.map(inboundSchool, school);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get().getStreet1()).isEqualTo(street1);
        assertThat(result.get().getStreet2()).isEqualTo(street2);
        assertThat(result.get().getCity()).isEqualTo(city);
        assertThat(result.get().getState()).isEqualTo(state);
        assertThat(result.get().getZip()).isEqualTo(zip);
        assertThat(result.get().getName()).isEqualTo(name);
        assertThat(result.get().getPhone()).isEqualTo(phoneService.normalize(phone));
        assertThat(result.get().getDistrict()).isEqualTo(district);
        assertThat(result.get().getIsPrivate()).isEqualTo(isPrivate);
        assertThat(result.get().getIsActive()).isEqualTo(true);
        assertThat(result.get().getEmailTag()).isEqualTo(emailTag);
    }

    @Test
    void testNull() throws Exception {
        // set up the fixture
        School school = new School();

        SchoolModelToEntityUpdateMapper schoolModelToEntityUpdateMapper = new SchoolModelToEntityUpdateMapper(
                new AddressModelToEntityUpdateMapper(), new PhoneService());

        // execute the SUT
        Optional<School> result = schoolModelToEntityUpdateMapper.map(null, school);

        // validation
        assertThat(result).isEmpty();
    }

}
