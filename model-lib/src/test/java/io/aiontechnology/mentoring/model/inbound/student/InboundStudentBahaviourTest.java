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

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundStudentBahaviourTest extends BaseValidatorTest {

    private static Stream<Pair<InboundStudentBehavior, String>> studentBehaviorInstanceProvider() {
        Set<String> behaviours = new HashSet<>();
        behaviours.add("TEST");
        InboundStudentBehavior nullStudentBehavior = InboundStudentBehavior.builder()
                .withBehaviors(null)
                .withTeacher(URI.create("http://test.com"))
                .build();
        InboundStudentBehavior nullTeacher = InboundStudentBehavior.builder()
                .withBehaviors(behaviours)
                .withTeacher(null)
                .build();
        return Stream.of(ImmutablePair.of(nullStudentBehavior, "{studentbehavior.behavior.notNull}"),
                ImmutablePair.of(nullTeacher, "{studentbehavior.teacher.notNull}"));
    }

    @ParameterizedTest
    @MethodSource("studentBehaviorInstanceProvider")
    void testInvalid(Pair<InboundStudentBehavior, String> studentBehaviorInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundStudentBehavior>> constraintViolations =
                getValidator().validate(studentBehaviorInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(studentBehaviorInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        Set<String> behaviours = new HashSet<>();
        behaviours.add("TEST");
        InboundStudentBehavior inboundStudentBehavior = InboundStudentBehavior.builder()
                .withBehaviors(behaviours)
                .withTeacher(URI.create("http://test.com"))
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentBehavior>> constraintViolations =
                getValidator().validate(inboundStudentBehavior);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
