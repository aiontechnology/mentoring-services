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

package io.aiontechnology.mentorsuccess.model.inbound;

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
public class InboundSchoolTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundSchool, String>> schoolInstanceProvider() {
        InboundSchool nullName = InboundSchool.builder()
                .withName(null)
                .build();
        InboundSchool nameTooLong = InboundSchool.builder()
                .withName(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 101 characters
                .build();
        InboundSchool phoneTooShort = InboundSchool.builder()
                .withName("NAME")
                .withPhone("INVALID")
                .build();
        InboundSchool districtTooLong = InboundSchool.builder()
                .withName("NAME")
                .withDistrict("123456789012345678901234567890123456789012345678901") // 51 characters
                .build();
        return Stream.of(ImmutablePair.of(nullName, "{school.name.notNull}"),
                ImmutablePair.of(nameTooLong, "{school.name.size}"),
                ImmutablePair.of(phoneTooShort, "{school.phone.invalid}"),
                ImmutablePair.of(districtTooLong, "{school.district.size}"));
    }

    @ParameterizedTest
    @MethodSource("schoolInstanceProvider")
    void testInvalid(Pair<InboundSchool, String> schoolInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundSchool>> constraintViolations =
                getValidator().validate(schoolInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(schoolInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundSchool inboundSchool = InboundSchool.builder()
                .withName("NAME")
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundSchool>> constraintViolations = getValidator().validate(inboundSchool);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
