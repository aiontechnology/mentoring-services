/*
 * Copyright 2022-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.model.inbound;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundAddressTest extends BaseValidatorTest {

    private static Stream<Pair<InboundAddress, String>> addressInstanceProvider() {
        InboundAddress street1TooLong = InboundAddress.builder()
                .withStreet1("123456789012345678901234567890123456789012345678901") // 51 characters
                .build();
        InboundAddress street2TooLong = InboundAddress.builder()
                .withStreet2("123456789012345678901234567890123456789012345678901") // 51 characters
                .build();
        InboundAddress cityTooLong = InboundAddress.builder()
                .withCity("123456789012345678901234567890123456789012345678901") // 51 characters
                .build();
        InboundAddress stateTooShort = InboundAddress.builder()
                .withState("1") // 1 character
                .build();
        InboundAddress stateTooLong = InboundAddress.builder()
                .withState("123") // 3 characters
                .build();
        InboundAddress zipTooLong = InboundAddress.builder()
                .withZip("1234567890") // 10 characters
                .build();
        InboundAddress zipTooShort = InboundAddress.builder()
                .withZip("1234") // 4 characters
                .build();
        return Stream.of(ImmutablePair.of(street1TooLong, "{address.street1.size}"),
                ImmutablePair.of(street2TooLong, "{address.street2.size}"),
                ImmutablePair.of(cityTooLong, "{address.city.size}"),
                ImmutablePair.of(stateTooShort, "{address.state.size}"),
                ImmutablePair.of(stateTooLong, "{address.state.size}"),
                ImmutablePair.of(zipTooShort, "{address.zip.size}"),
                ImmutablePair.of(zipTooLong, "{address.zip.size}"));
    }

    @ParameterizedTest
    @MethodSource("addressInstanceProvider")
    void testInvalid(Pair<InboundAddress, String> addressInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundAddress>> constraintViolations =
                getValidator().validate(addressInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(addressInstance.getRight());
    }

    @Test
    void testValid() {
        // setup the fixture
        InboundAddress inboundAddress = InboundAddress.builder()
                .withStreet1("STREET1")
                .withStreet2("STREET2")
                .withCity("CITY")
                .withState("ST")
                .withZip("98682")
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundAddress>> constraintViolations =
                getValidator().validate(inboundAddress);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
