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
public class InboundTeacherTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundTeacher, String>> teacherInstanceProvider() {
        InboundTeacher nullFirstName = InboundTeacher.builder()
                .withFirstName(null)
                .withLastName("LAST")
                .build();
        InboundTeacher firstNameTooLong = InboundTeacher.builder()
                .withFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withLastName("LAST")
                .build();
        InboundTeacher nullLastName = InboundTeacher.builder()
                .withFirstName("FIRST")
                .withLastName(null)
                .build();
        InboundTeacher lastNameTooLong = InboundTeacher.builder()
                .withFirstName("FIRST")
                .withLastName("123456789012345678901234567890123456789012345678901") // 51 characters
                .build();
        InboundTeacher invalidEmail = InboundTeacher.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("invalid")
                .build();
        InboundTeacher invalidWorkPhone = InboundTeacher.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withWorkPhone("invalid")
                .build();
        InboundTeacher invalidCellPhone = InboundTeacher.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withCellPhone("invalid")
                .build();
        return Stream.of(ImmutablePair.of(nullFirstName, "{teacher.firstName.notNull}"),
                ImmutablePair.of(firstNameTooLong, "{teacher.firstName.size}"),
                ImmutablePair.of(nullLastName, "{teacher.lastName.notNull}"),
                ImmutablePair.of(lastNameTooLong, "{teacher.lastName.size}"),
                ImmutablePair.of(invalidEmail, "{teacher.email.invalid}"),
                ImmutablePair.of(invalidWorkPhone, "{teacher.workPhone.invalid}"),
                ImmutablePair.of(invalidCellPhone, "{teacher.cellPhone.invalid}"));
    }

    @ParameterizedTest
    @MethodSource("teacherInstanceProvider")
    void testInvalid(Pair<InboundTeacher, String> teacherInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundTeacher>> constraintViolations =
                getValidator().validate(teacherInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(teacherInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundTeacher inboundTeacher = InboundTeacher.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("test@email.com")
                .withWorkPhone("(123) 456-7890")
                .withCellPhone("(123) 456-7890")
                .withGrade1(1)
                .withGrade2(2)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundTeacher>> constraintViolations = getValidator().validate(inboundTeacher);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
