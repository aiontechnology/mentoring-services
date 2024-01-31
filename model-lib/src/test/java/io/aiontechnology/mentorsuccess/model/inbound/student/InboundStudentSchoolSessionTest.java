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

import static io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation.BOTH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundStudentSchoolSessionTest extends BaseValidatorTest {

    private static Stream<Pair<InboundStudentSchoolSession, String>> studentTeacherInstanceProvider() {
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(URI.create("http://test.com"))
                .build();
        InboundStudentSchoolSession nullStudent = InboundStudentSchoolSession.builder()
                .withStudent(null)
                .withGrade(1)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession nullGrade = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(null)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession gradeTooSmall = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(-1)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession gradeTooLarge = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(6)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession preferredTimeTooLong = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(1)
                .withPreferredTime("1234567890123456789012345678901") // 31 characters
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession actualTimeTooLong = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(1)
                .withPreferredTime("PREFERRED")
                .withActualTime("1234567890123456789012345678901") // 31 characters
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession nullLocation = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(1)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(null)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession nullMediaRelease = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(1)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(null)
                .withTeacher(inboundStudentTeacher)
                .build();
        InboundStudentSchoolSession nullTeacher = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(1)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(null)
                .build();
        return Stream.of(ImmutablePair.of(nullStudent, "{studentsession.student.notNull}"),
                ImmutablePair.of(nullGrade, "{studentsession.grade.notNull}"),
                ImmutablePair.of(gradeTooSmall, "{studentsession.grade.invalid}"),
                ImmutablePair.of(gradeTooLarge, "{studentsession.grade.invalid}"),
                ImmutablePair.of(preferredTimeTooLong, "{studentsession.preferredTime.size}"),
                ImmutablePair.of(actualTimeTooLong, "{studentsession.actualTime.size}"),
                ImmutablePair.of(nullLocation, "{studentsession.location.notNull}"),
                ImmutablePair.of(nullMediaRelease, "{studentsession.mediaRelease.notNull}"),
                ImmutablePair.of(nullTeacher, "{studentsession.teacher.notNull}"));
    }

    @ParameterizedTest
    @MethodSource("studentTeacherInstanceProvider")
    void testInvalid(Pair<InboundStudentSchoolSession, String> studentSessionInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundStudentSchoolSession>> constraintViolations =
                getValidator().validate(studentSessionInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(studentSessionInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(URI.create("http://test.com"))
                .build();
        InboundStudentSchoolSession inboundStudentSchoolSession = InboundStudentSchoolSession.builder()
                .withStudent(URI.create("http://test.com"))
                .withGrade(1)
                .withPreferredTime("PREFERRED")
                .withActualTime("ACTUAL")
                .withLocation(BOTH)
                .withRegistrationSigned(true)
                .withMediaReleaseSigned(true)
                .withTeacher(inboundStudentTeacher)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentSchoolSession>> constraintViolations =
                getValidator().validate(inboundStudentSchoolSession);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
