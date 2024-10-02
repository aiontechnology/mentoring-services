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

import io.aiontechnology.mentoring.model.inbound.student.InboundContact;
import io.aiontechnology.mentoring.model.validation.ContactValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class ContactValidatorTest {

    @Test
    void testNullEmail() {
        // set up the fixture
        InboundContact inboundContact = InboundContact.builder()
                .withEmail(null)
                .withPhone("1234567890")
                .build();

        ContactValidator contactValidator = new ContactValidator();

        // execute the SUT
        boolean result = contactValidator.isValid(inboundContact, null);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testNullPhone() {
        // set up the fixture
        InboundContact inboundContact = InboundContact.builder()
                .withEmail("test@test.com")
                .withPhone(null)
                .build();

        ContactValidator contactValidator = new ContactValidator();

        // execute the SUT
        boolean result = contactValidator.isValid(inboundContact, null);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testNullEmailAndPhone() {
        // set up the fixture
        InboundContact inboundContact = InboundContact.builder()
                .withEmail(null)
                .withPhone(null)
                .build();

        ContactValidator contactValidator = new ContactValidator();

        // execute the SUT
        boolean result = contactValidator.isValid(inboundContact, null);

        // validation
        assertThat(result).isFalse();
    }

}
