/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.validation;

import io.aiontechnology.mentoring.model.validation.GradeRangeHolder;
import io.aiontechnology.mentoring.model.validation.GradeRangeValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class GradeRangeValidatorTest {

    @Test
    void testNullGrade1() {
        // set up the fixture
        GradeRangeHolder holder = new GradeRangeHolder() {
            @Override
            public Integer getGrade1() {
                return null;
            }

            @Override
            public Integer getGrade2() {
                return 1;
            }
        };

        GradeRangeValidator gradeRangeValidator = new GradeRangeValidator();

        // execute the SUT
        boolean result = gradeRangeValidator.isValid(holder, null);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testNullGrade2() {
        // set up the fixture
        GradeRangeHolder holder = new GradeRangeHolder() {
            @Override
            public Integer getGrade1() {
                return 1;
            }

            @Override
            public Integer getGrade2() {
                return null;
            }
        };

        GradeRangeValidator gradeRangeValidator = new GradeRangeValidator();

        // execute the SUT
        boolean result = gradeRangeValidator.isValid(holder, null);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testNullGrade1andGrade2() {
        // set up the fixture
        GradeRangeHolder holder = new GradeRangeHolder() {
            @Override
            public Integer getGrade1() {
                return null;
            }

            @Override
            public Integer getGrade2() {
                return null;
            }
        };

        GradeRangeValidator gradeRangeValidator = new GradeRangeValidator();

        // execute the SUT
        boolean result = gradeRangeValidator.isValid(holder, null);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testGrade1SmallerThanGrade2() {
        // set up the fixture
        GradeRangeHolder holder = new GradeRangeHolder() {
            @Override
            public Integer getGrade1() {
                return 1;
            }

            @Override
            public Integer getGrade2() {
                return 2;
            }
        };

        GradeRangeValidator gradeRangeValidator = new GradeRangeValidator();

        // execute the SUT
        boolean result = gradeRangeValidator.isValid(holder, null);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testGrade1EqualToGrade2() {
        // set up the fixture
        GradeRangeHolder holder = new GradeRangeHolder() {
            @Override
            public Integer getGrade1() {
                return 1;
            }

            @Override
            public Integer getGrade2() {
                return 1;
            }
        };

        GradeRangeValidator gradeRangeValidator = new GradeRangeValidator();

        // execute the SUT
        boolean result = gradeRangeValidator.isValid(holder, null);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testGrade1LargerThanGrade2() {
        // set up the fixture
        GradeRangeHolder holder = new GradeRangeHolder() {
            @Override
            public Integer getGrade1() {
                return 2;
            }

            @Override
            public Integer getGrade2() {
                return 1;
            }
        };

        GradeRangeValidator gradeRangeValidator = new GradeRangeValidator();

        // execute the SUT
        boolean result = gradeRangeValidator.isValid(holder, null);

        // validation
        assertThat(result).isFalse();
    }

}
