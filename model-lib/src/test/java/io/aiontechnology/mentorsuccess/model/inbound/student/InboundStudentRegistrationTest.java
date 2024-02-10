/*
 * Copyright 2024 Aion Technology LLC
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
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class InboundStudentRegistrationTest extends BaseValidatorTest {

    @Test
    void testBuildValid() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("FIRST_NAME")
                .withStudentLastName("LAST_NAME")
                .withGrade(1)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.isEmpty()).isTrue();
    }

    @Test
    void testFirstNameMissing() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentLastName("LAST_NAME")
                .withGrade(1)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.firstname.notNull}");
    }

    @Test
    void testFirstNameTooLong() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("123456789012345678901234567890123456789012345678901")
                .withStudentLastName("LAST_NAME")
                .withGrade(1)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.firstname.size}");
    }

    @Test
    void testLastNameMissing() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("FIRST_NAME")
                .withGrade(1)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.lastname.notNull}");
    }

    @Test
    void testLastNameTooLong() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("FIRST_NAME")
                .withStudentLastName("123456789012345678901234567890123456789012345678901")
                .withGrade(1)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.lastname.size}");
    }

    @Test
    void testGradeMissing() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("FIRST_NAME")
                .withStudentLastName("LAST_NAME")
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.grade.notNull}");
    }

    @Test
    void testGradeTooSmall() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("FIRST_NAME")
                .withStudentLastName("LAST_NAME")
                .withGrade(-1)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.grade.invalid}");
    }

    @Test
    void testGradeTooLarge() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("FIRST_NAME")
                .withStudentLastName("LAST_NAME")
                .withGrade(6)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.grade.invalid}");
    }

    @Test
    void testTeacherMissing() {
        // set up the fixture
        var inboundStudentRegistration = InboundStudentRegistration.builder()
                .withStudentFirstName("FIRST_NAME")
                .withStudentLastName("LAST_NAME")
                .withGrade(1)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentRegistration>> constraintViolations =
                getValidator().validate(inboundStudentRegistration);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo("{registration.student.teacher.notNull}");
    }

}
