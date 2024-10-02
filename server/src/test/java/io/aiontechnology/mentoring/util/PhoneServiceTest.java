/*
 * Copyright 2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhoneServiceTest {

    @Test
    void testFormat() {
        // set up the fixture
        var phoneNumber = "1234567890";
        var phoneService = new PhoneService();

        // execute the SUT
        var result = phoneService.format(phoneNumber);

        // validation
        assertThat(result).isEqualTo("(123) 456-7890");
    }

    @Test
    void testFormatAlreadyFormatted() {
        // set up the fixture
        var phoneNumber = "(123) 456-7890";
        var phoneService = new PhoneService();

        // execute the SUT
        var result = phoneService.format(phoneNumber);

        // validation
        assertThat(result).isEqualTo("(123) 456-7890");
    }

    @Test
    void testFormatInvalid() {
        // set up the fixture
        var phoneNumber = "INVALID";
        var phoneService = new PhoneService();

        // execute the SUT
        var result = assertThrows(IllegalArgumentException.class, () -> phoneService.format(phoneNumber));

        // validation
        assertThat(result.getMessage()).isEqualTo("Unable to normalize phone number: INVALID");
    }

    @Test
    void testFormatNull() {
        // set up the fixture
        String phoneNumber = null;
        var phoneService = new PhoneService();

        // execute the SUT
        var result = phoneService.format(phoneNumber);

        // validation
        assertThat(result).isNull();
    }

    @Test
    void testNormalize() {
        // set up the fixture
        var phoneNumber = "(123) 456-7890";
        var phoneService = new PhoneService();

        // execute the SUT
        var result = phoneService.normalize(phoneNumber);

        // validation
        assertThat(result).isEqualTo("1234567890");
    }

    @Test
    void testNormalizeAlreadyNormalized() {
        // set up the fixture
        var phoneNumber = "1234567890";
        var phoneService = new PhoneService();

        // execute the SUT
        var result = phoneService.normalize(phoneNumber);

        // validation
        assertThat(result).isEqualTo("1234567890");
    }

    @Test
    void testNormalizeNull() {
        // set up the fixture
        String phoneNumber = null;
        var phoneService = new PhoneService();

        // execute the SUT
        var result = phoneService.normalize(phoneNumber);

        // validation
        assertThat(result).isNull();
    }

}
