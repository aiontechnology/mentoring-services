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

import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.outbound.OutboundAddress;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AddressEntityToModelMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class AddressEntityToModelMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        String street1 = "STREET1";
        String street2 = "STREET2";
        String city = "CITY";
        String state = "STATE";
        String zip = "ZIP";

        School school = new School();
        school.setStreet1(street1);
        school.setStreet2(street2);
        school.setCity(city);
        school.setState(state);
        school.setZip(zip);

        AddressEntityToModelMapper addressEntityToModelMapper = new AddressEntityToModelMapper();

        // execute the SUT
        Optional<OutboundAddress> result = addressEntityToModelMapper.map(school);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get().getStreet1()).isEqualTo(street1);
        assertThat(result.get().getStreet2()).isEqualTo(street2);
        assertThat(result.get().getCity()).isEqualTo(city);
        assertThat(result.get().getState()).isEqualTo(state);
        assertThat(result.get().getZip()).isEqualTo(zip);
    }

    @Test
    void testNull() throws Exception {
        // setup the fixture
        AddressEntityToModelMapper addressEntityToModelMapper = new AddressEntityToModelMapper();

        // execute the SUT
        Optional<OutboundAddress> result = addressEntityToModelMapper.map(null);

        // validation
        assertThat(result).isEmpty();
    }

}
