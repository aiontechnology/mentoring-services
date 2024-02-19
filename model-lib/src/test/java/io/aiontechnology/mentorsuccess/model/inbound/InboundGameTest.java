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
public class InboundGameTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundGame, String>> gameInstanceProvider() {
        InboundGame nullName = InboundGame.builder()
                .withGrade1(1)
                .withGrade2(2)
                .withLocation(OFFLINE)
                .build();
        InboundGame nameTooLong = InboundGame.builder()
                .withName(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901") // 101 characters
                .withGrade1(1)
                .withGrade2(2)
                .withLocation(OFFLINE)
                .build();
        InboundGame nullGrade1 = InboundGame.builder()
                .withName("NAME")
                .withGrade1(null)
                .withGrade2(2)
                .withLocation(OFFLINE)
                .build();
        InboundGame nullGrade2 = InboundGame.builder()
                .withName("NAME")
                .withGrade1(1)
                .withGrade2(null)
                .withLocation(OFFLINE)
                .build();
        InboundGame grade1LargerThanGrade2 = InboundGame.builder()
                .withName("NAME")
                .withGrade1(2)
                .withGrade2(1)
                .withLocation(OFFLINE)
                .build();
        return Stream.of(ImmutablePair.of(nullName, "{game.name.notNull}"),
                ImmutablePair.of(nameTooLong, "{game.name.size}"),
                ImmutablePair.of(nullGrade1, "{game.grade1.notNull}"),
                ImmutablePair.of(nullGrade2, "{game.grade2.notNull}"),
                ImmutablePair.of(grade1LargerThanGrade2, "{grade.range}"));
    }

    @Test
    void testGradesTooLarge() {
        // set up the fixture
        InboundGame gradesTooSmall = InboundGame.builder()
                .withName("NAME")
                .withGrade1(13)
                .withGrade2(13)
                .withLocation(OFFLINE)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundGame>> constraintViolations = getValidator().validate(gradesTooSmall);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(2);
        assertThat(constraintViolations.stream().anyMatch(g -> g.getMessage().equals("{game.grade1.invalid}")));
        assertThat(constraintViolations.stream().anyMatch(g -> g.getMessage().equals("{game.grade2.invalid}")));
    }

    @Test
    void testGradesTooSmall() {
        // set up the fixture
        InboundGame gradesTooSmall = InboundGame.builder()
                .withName("NAME")
                .withGrade1(0)
                .withGrade2(0)
                .withLocation(OFFLINE)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundGame>> constraintViolations = getValidator().validate(gradesTooSmall);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(2);
        assertThat(constraintViolations.stream().anyMatch(g -> g.getMessage().equals("{game.grade1.invalid}")));
        assertThat(constraintViolations.stream().anyMatch(g -> g.getMessage().equals("{game.grade2.invalid}")));
    }

    @ParameterizedTest
    @MethodSource("gameInstanceProvider")
    void testInvalid(Pair<InboundGame, String> gameInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundGame>> constraintViolations = getValidator().validate(gameInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(gameInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundGame inboundGame = InboundGame.builder()
                .withName("NAME")
                .withGrade1(1)
                .withGrade2(2)
                .withLocation(OFFLINE)
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundGame>> constraintViolations =
                getValidator().validate(inboundGame);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
