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
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundBookTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundBook, String>> bookInstanceProvider() {
        InboundBook nullTitle = InboundBook.builder()
                .withTitle(null)
                .withLocation(OFFLINE)
                .withGradeLevel(1)
                .build();
        InboundBook titleTooLong = InboundBook.builder()
                .withTitle(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 101 characters
                .withLocation(OFFLINE)
                .withGradeLevel(1)
                .build();
        InboundBook authorTooLong = InboundBook.builder()
                .withTitle("TITLE")
                .withAuthor("1234567890123456789012345678901") // 31 characters
                .withLocation(OFFLINE)
                .withGradeLevel(1)
                .build();
        InboundBook nullGrade = InboundBook.builder()
                .withTitle("TITLE")
                .withLocation(OFFLINE)
                .withGradeLevel(null)
                .build();
        InboundBook gradeTooSmall = InboundBook.builder()
                .withTitle("TITLE")
                .withLocation(OFFLINE)
                .withGradeLevel(0)
                .build();
        InboundBook gradeTooLarge = InboundBook.builder()
                .withTitle("TITLE")
                .withLocation(OFFLINE)
                .withGradeLevel(13)
                .build();
        InboundBook nullLocation = InboundBook.builder()
                .withTitle("TITLE")
                .withLocation(null)
                .withGradeLevel(1)
                .build();
        return Stream.of(ImmutablePair.of(nullTitle, "{book.title.notNull}"),
                ImmutablePair.of(titleTooLong, "{book.title.size}"),
                ImmutablePair.of(authorTooLong, "{book.author.size}"),
                ImmutablePair.of(nullGrade, "{book.gradeLevel.notNull}"),
                ImmutablePair.of(gradeTooSmall, "{book.gradeLevel.invalid}"),
                ImmutablePair.of(gradeTooLarge, "{book.gradeLevel.invalid}"),
                ImmutablePair.of(nullLocation, "{book.location.notNull}"));
    }

    @ParameterizedTest
    @MethodSource("bookInstanceProvider")
    void testInvalid(Pair<InboundBook, String> bookInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundBook>> constraintViolations = getValidator().validate(bookInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(bookInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundBook inboundBook = InboundBook.builder()
                .withTitle("TITLE")
                .withAuthor("AUTHOR")
                .withGradeLevel(1)
                .withLocation(OFFLINE)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundBook>> constraintViolations = getValidator().validate(inboundBook);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
