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
public class InboundPersonTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundPerson, String>> personInstanceProvider() {
        InboundPerson nullFirstName = InboundPerson.builder()
                .withFirstName(null)
                .withLastName("LAST")
                .build();
        InboundPerson firstNameTooLong = InboundPerson.builder()
                .withFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withLastName("LAST")
                .build();
        InboundPerson nullLastName = InboundPerson.builder()
                .withFirstName("FIRST")
                .withLastName(null)
                .build();
        InboundPerson lastNameTooLong = InboundPerson.builder()
                .withFirstName("FIRST")
                .withLastName("123456789012345678901234567890123456789012345678901") // 51 characters
                .build();
        InboundPerson invalidEmail = InboundPerson.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("invalid")
                .build();
        InboundPerson invalidWorkPhone = InboundPerson.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withWorkPhone("invalid")
                .build();
        InboundPerson invalidCellPhone = InboundPerson.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withCellPhone("invalid")
                .build();
        return Stream.of(ImmutablePair.of(nullFirstName, "{person.firstName.notNull}"),
                ImmutablePair.of(firstNameTooLong, "{person.firstName.size}"),
                ImmutablePair.of(nullLastName, "{person.lastName.notNull}"),
                ImmutablePair.of(lastNameTooLong, "{person.lastName.size}"),
                ImmutablePair.of(invalidEmail, "{person.email.invalid}"),
                ImmutablePair.of(invalidWorkPhone, "{person.workPhone.invalid}"),
                ImmutablePair.of(invalidCellPhone, "{person.cellPhone.invalid}"));
    }

    @ParameterizedTest
    @MethodSource("personInstanceProvider")
    void testInvalid(Pair<InboundPerson, String> personInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundPerson>> constraintViolations =
                getValidator().validate(personInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(personInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundPerson inboundPerson = InboundPerson.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundPerson>> constraintViolations = getValidator().validate(inboundPerson);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
