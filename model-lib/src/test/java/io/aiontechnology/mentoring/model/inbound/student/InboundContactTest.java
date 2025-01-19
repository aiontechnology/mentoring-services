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

package io.aiontechnology.mentoring.model.inbound.student;

import io.aiontechnology.mentoring.model.inbound.BaseValidatorTest;
import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static io.aiontechnology.mentoring.model.enumeration.ContactMethod.EITHER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundContactTest extends BaseValidatorTest {

    private static Stream<Pair<InboundContact, String>> contactInstanceProvider() {
        InboundContact labelTooLong = InboundContact.builder()
                .withLabel("123456789012345678901234567890123456789012345678901") // 51 characters
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact nullFirstName = InboundContact.builder()
                .withFirstName(null)
                .withLastName("LAST")
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact firstNameTooLong = InboundContact.builder()
                .withFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withLastName("LAST")
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact nullLastName = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName(null)
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact lastNameTooLong = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact emailPattern = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("invalid")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact emailTooLong = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("123456789012345678901234567890123456789012@test.com") // 51 characters
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact phonePattern = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withPhone("invalid")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();
        InboundContact nullIsEmergencyContact = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(null)
                .build();
        InboundContact commentToLong = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .withComment(
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456") // 256 characters
                .build();
        return Stream.of(ImmutablePair.of(labelTooLong, "{contact.label.size}"),
                ImmutablePair.of(nullFirstName, "{contact.firstName.notNull}"),
                ImmutablePair.of(firstNameTooLong, "{contact.firstName.size}"),
                ImmutablePair.of(nullLastName, "{contact.lastName.notNull}"),
                ImmutablePair.of(lastNameTooLong, "{contact.lastName.size}"),
                ImmutablePair.of(emailPattern, "{contact.email.invalid}"),
                ImmutablePair.of(emailTooLong, "{contact.email.size}"),
                ImmutablePair.of(phonePattern, "{contact.phone.invalid}"),
                ImmutablePair.of(nullIsEmergencyContact, "{contact.isEmergencyContact.notNull}"),
                ImmutablePair.of(commentToLong, "{contact.comment.size}"));
    }

    @ParameterizedTest
    @MethodSource("contactInstanceProvider")
    void testInvalid(Pair<InboundContact, String> contactInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundContact>> constraintViolations =
                getValidator().validate(contactInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(contactInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundContact inboundContact = InboundContact.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("test@test.com")
                .withPreferredContactMethod(EITHER)
                .withIsEmergencyContact(true)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundContact>> constraintViolations = getValidator().validate(inboundContact);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
