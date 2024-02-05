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

import static io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation.OFFLINE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundMentorTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundMentor, String>> mentorInstanceProvider() {
        InboundMentor nullFirstName = InboundMentor.builder()
                .withFirstName(null)
                .withLastName("LAST")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor firstNameTooLong = InboundMentor.builder()
                .withFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withLastName("LAST")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor nullLastName = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName(null)
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor lastNameTooLong = InboundMentor.builder()
                .withFirstName("FIRST") // 51 characters
                .withLastName("123456789012345678901234567890123456789012345678901")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor invalidEmailAddress = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("invalid@")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor invalidWorkPhone = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withWorkPhone("invalid")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor invalidCellPhone = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withCellPhone("invalid")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor availabilityTooLong = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withAvailability(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 101 characters
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor nullLocation = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withLocation(null)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor nullMediaRelease = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(null)
                .withBackgroundCheckCompleted(TRUE)
                .build();
        InboundMentor nullBackgroundCheck = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(null)
                .build();
        return Stream.of(ImmutablePair.of(nullFirstName, "{mentor.firstName.notNull}"),
                ImmutablePair.of(firstNameTooLong, "{mentor.firstName.size}"),
                ImmutablePair.of(nullLastName, "{mentor.lastName.notNull}"),
                ImmutablePair.of(lastNameTooLong, "{mentor.lastName.size}"),
                ImmutablePair.of(invalidEmailAddress, "{mentor.email.invalid}"),
                ImmutablePair.of(invalidWorkPhone, "{mentor.workPhone.invalid}"),
                ImmutablePair.of(invalidCellPhone, "{mentor.cellPhone.invalid}"),
                ImmutablePair.of(availabilityTooLong, "{mentor.availability.size}"),
                ImmutablePair.of(nullLocation, "{mentor.location.notNull}"),
                ImmutablePair.of(nullMediaRelease, "{mentor.mediaRelease.notNull}"),
                ImmutablePair.of(nullBackgroundCheck, "{mentor.backgroundCheckCompleted.notNull}"));
    }

    @ParameterizedTest
    @MethodSource("mentorInstanceProvider")
    void testInvalid(Pair<InboundMentor, String> mentorInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundMentor>> constraintViolations =
                getValidator().validate(mentorInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(mentorInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundMentor inboundMentor = InboundMentor.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withLocation(OFFLINE)
                .withMediaReleaseSigned(TRUE)
                .withBackgroundCheckCompleted(TRUE)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundMentor>> constraintViolations = getValidator().validate(inboundMentor);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
