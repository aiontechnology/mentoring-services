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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundStudentTeacherTest extends BaseValidatorTest {

    private static Stream<Pair<InboundStudentTeacher, String>> studentTeacherInstanceProvider() {
        InboundStudentTeacher nullUri = InboundStudentTeacher.builder()
                .withUri(null)
                .build();
        InboundStudentTeacher commentTooLong = InboundStudentTeacher.builder()
                .withUri(URI.create("http://test.com"))
                .withComment(
                        "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 501 characters
                .build();
        return Stream.of(ImmutablePair.of(nullUri, "{studentteacher.uri.notNull}"),
                ImmutablePair.of(commentTooLong, "{studentteacher.comment.size}"));
    }

    @ParameterizedTest
    @MethodSource("studentTeacherInstanceProvider")
    void testInvalid(Pair<InboundStudentTeacher, String> studentTeacherInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundStudentTeacher>> constraintViolations =
                getValidator().validate(studentTeacherInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(studentTeacherInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentTeacher>> constraintViolations =
                getValidator().validate(inboundStudentTeacher);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
