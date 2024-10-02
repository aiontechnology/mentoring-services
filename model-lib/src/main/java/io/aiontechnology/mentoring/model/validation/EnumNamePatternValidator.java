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

package io.aiontechnology.mentoring.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validator for the {@link EnumNamePattern}.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, Enum<?>> {

    /** The regular expression pattern to validate with */
    private Pattern pattern;

    /**
     * Initialize the validator.
     *
     * @param constraintAnnotation The {@link EnumNamePattern} from which to take the regular expression.
     */
    @Override
    public void initialize(EnumNamePattern constraintAnnotation) {
        pattern = Pattern.compile(constraintAnnotation.regexp());
    }

    /**
     * Determine if the validation is valid of not.
     *
     * @param value The enumeration value to validate.
     * @param context The {@link ConstraintValidatorContext}.
     * @return True if the validation passes.
     */
    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        return value == null || pattern.matcher(value.name()).matches();
    }

}
