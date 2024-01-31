/*
 * Copyright 2020-2024 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator that validates {@link GradeRangeHolder} objects.
 *
 * @author Whitney Hunter
 * @since 0.2.0
 */
public class GradeRangeValidator implements ConstraintValidator<GradeRange, GradeRangeHolder> {

    @Override
    public boolean isValid(GradeRangeHolder gradeRange, ConstraintValidatorContext context) {
        if (gradeRange.getGrade1() == null || gradeRange.getGrade2() == null) {
            return true;
        }
        return (gradeRange.getGrade1() <= gradeRange.getGrade2());
    }

}
