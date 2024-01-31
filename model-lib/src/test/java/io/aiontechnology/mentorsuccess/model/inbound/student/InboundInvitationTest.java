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

package io.aiontechnology.mentorsuccess.model.inbound.student;

import io.aiontechnology.mentorsuccess.model.inbound.BaseValidatorTest;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
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
public class InboundInvitationTest extends BaseValidatorTest {

    private static Stream<Pair<InboundInvitation, String>> studentRegistrationInstanceProvider() {
        InboundInvitation nullStudentFirstName = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName(null)
                .withStudentLastName("STUDENT_LAST")
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress("test@test.com")
                .build();
        InboundInvitation studentFirstNameTooLong = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withStudentLastName("STUDENT_LAST")
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress("test@test.com")
                .build();
        InboundInvitation nullStudentLastName = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName("STUDENT_FIRST")
                .withStudentLastName(null)
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress("test@test.com")
                .build();
        InboundInvitation studentLastNameTooLong = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName("STUDENT_FIRST")
                .withStudentLastName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress("test@test.com")
                .build();
        InboundInvitation nullParentEmail = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName("STUDENT_FIRST")
                .withStudentLastName("STUDENT_LAST")
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress(null)
                .build();
        InboundInvitation parentEmailTooLong = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName("STUDENT_FIRST")
                .withStudentLastName("STUDENT_LAST")
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress("123456789012345678901234567890123456789012@test.com") // 51 characters
                .build();
        InboundInvitation parentEmailPattern = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName("STUDENT_FIRST")
                .withStudentLastName("STUDENT_LAST")
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress("invalid")
                .build();
        return Stream.of(ImmutablePair.of(nullStudentFirstName, "{registration.student.firstname.notNull}"),
                ImmutablePair.of(studentFirstNameTooLong, "{registration.student.firstname.size}"),
                ImmutablePair.of(nullStudentLastName, "{registration.student.lastname.notNull}"),
                ImmutablePair.of(studentLastNameTooLong, "{registration.student.lastname.size}"),
                ImmutablePair.of(nullParentEmail, "{registration.parent1.email.notNull}"),
                ImmutablePair.of(parentEmailTooLong, "{registration.parent1.email.size}"),
                ImmutablePair.of(parentEmailPattern, "{registration.parent1.email.invalid}"));
    }

    @ParameterizedTest
    @MethodSource("studentRegistrationInstanceProvider")
    void testInvalid(Pair<InboundInvitation, String> studentRegistrationInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundInvitation>> constraintViolations =
                getValidator().validate(studentRegistrationInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(studentRegistrationInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundInvitation inboundInvitation = InboundInvitation.builder()
                .withStudentRegistrationUri("URI")
                .withStudentFirstName("STUDENT_FIRST")
                .withStudentLastName("STUDENT_LAST")
                .withParent1FirstName("PARENT_FIRST")
                .withParent1LastName("PARENT_LAST")
                .withParent1EmailAddress("test@test.com")
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundInvitation>> constraintViolations =
                getValidator().validate(inboundInvitation);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
