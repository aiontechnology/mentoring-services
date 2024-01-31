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
public class InboundProgramAdminTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundProgramAdmin, String>> programAdminInstanceProvider() {
        InboundProgramAdmin nullFirstName = InboundProgramAdmin.builder()
                .withFirstName(null)
                .withLastName("LAST")
                .withEmail("test@test.com")
                .build();
        InboundProgramAdmin firstNameTooLong = InboundProgramAdmin.builder()
                .withFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withLastName("LAST")
                .withEmail("test@test.com")
                .build();
        InboundProgramAdmin nullLastName = InboundProgramAdmin.builder()
                .withFirstName("FIRST")
                .withLastName(null)
                .withEmail("test@test.com")
                .build();
        InboundProgramAdmin lastNameTooLong = InboundProgramAdmin.builder()
                .withFirstName("FIRST")
                .withLastName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withEmail("test@test.com")
                .build();
        InboundProgramAdmin nullEmail = InboundProgramAdmin.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail(null)
                .build();
        InboundProgramAdmin emailTooLong = InboundProgramAdmin.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("1234567890123456789012345678901234567890123456789012@test.com")
                .build();
        InboundProgramAdmin emailPattern = InboundProgramAdmin.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("INVALID")
                .build();
        return Stream.of(ImmutablePair.of(nullFirstName, "{programAdmin.firstName.notNull}"),
                ImmutablePair.of(firstNameTooLong, "{programAdmin.firstName.size}"),
                ImmutablePair.of(nullLastName, "{programAdmin.lastName.notNull}"),
                ImmutablePair.of(lastNameTooLong, "{programAdmin.lastName.size}"),
                ImmutablePair.of(nullEmail, "{programAdmin.email.notNull}"),
                ImmutablePair.of(emailTooLong, "{programAdmin.email.size}"),
                ImmutablePair.of(emailPattern, "{programAdmin.email.invalid}"));
    }

    @ParameterizedTest
    @MethodSource("programAdminInstanceProvider")
    void testInvalid(Pair<InboundProgramAdmin, String> programAdminInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundProgramAdmin>> constraintViolations =
                getValidator().validate(programAdminInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(programAdminInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundProgramAdmin inboundProgramAdmin = InboundProgramAdmin.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("test@test.com")
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundProgramAdmin>> constraintViolations =
                getValidator().validate(inboundProgramAdmin);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
