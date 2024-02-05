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
import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URI;
import java.util.Set;
import java.util.stream.Stream;

import static io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation.ONLINE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundStudentTest extends BaseValidatorTest {

    private static Stream<Pair<InboundStudent, String>> studentInstanceProvider() {
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(URI.create("http://test.com"))
                .build();
        InboundStudent idTooLong = InboundStudent.builder()
                .withStudentId("123456789012345678901") // 21 characters
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withGrade(1)
                .withLocation(ONLINE)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudent nullFirstName = InboundStudent.builder()
                .withFirstName(null)
                .withLastName("LAST")
                .withGrade(1)
                .withLocation(ONLINE)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudent firstNameTooLong = InboundStudent.builder()
                .withFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withLastName("LAST")
                .withGrade(1)
                .withLocation(ONLINE)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudent nullLastName = InboundStudent.builder()
                .withFirstName("FIRST")
                .withLastName(null)
                .withGrade(1)
                .withLocation(ONLINE)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudent lastNameTooLong = InboundStudent.builder()
                .withFirstName("FIRST")
                .withLastName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withGrade(1)
                .withLocation(ONLINE)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        return Stream.of(ImmutablePair.of(idTooLong, "{student.id.size}"),
                ImmutablePair.of(nullFirstName, "{student.firstname.notNull}"),
                ImmutablePair.of(firstNameTooLong, "{student.firstname.size}"),
                ImmutablePair.of(nullLastName, "{student.lastname.notNull}"),
                ImmutablePair.of(lastNameTooLong, "{student.lastname.size}"));
    }

    @ParameterizedTest
    @MethodSource("studentInstanceProvider")
    void testInvalid(Pair<InboundStudent, String> studentInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundStudent>> constraintViolations =
                getValidator().validate(studentInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(studentInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(URI.create("http://test.com"))
                .build();
        InboundStudent inboundStudent = InboundStudent.builder()
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withGrade(1)
                .withLocation(ONLINE)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudent>> constraintViolations = getValidator().validate(inboundStudent);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
