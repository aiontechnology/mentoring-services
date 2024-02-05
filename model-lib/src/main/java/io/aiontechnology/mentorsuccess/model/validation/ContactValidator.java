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

import io.aiontechnology.mentorsuccess.model.inbound.student.InboundContact;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validate that a {@link InboundContact} has at least one provided contact method.
 *
 * @author Whitney Hunter
 * @since 0.8.0
 */
public class ContactValidator implements ConstraintValidator<Contact, InboundContact> {

    @Override
    public boolean isValid(InboundContact contact, ConstraintValidatorContext context) {
        return contact.getEmail() != null || contact.getPhone() != null;
    }

}
