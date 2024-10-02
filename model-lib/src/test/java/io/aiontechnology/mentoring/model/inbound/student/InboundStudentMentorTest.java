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
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundStudentMentorTest extends BaseValidatorTest {

    @Test
    void testValid() {
        // set up the fixture
        InboundStudentMentor inboundStudentMentor = InboundStudentMentor.builder()
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundStudentMentor>> constraintViolations =
                getValidator().validate(inboundStudentMentor);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
