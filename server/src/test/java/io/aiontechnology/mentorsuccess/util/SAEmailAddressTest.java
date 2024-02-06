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

package io.aiontechnology.mentorsuccess.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SAEmailAddressTest {

    @Test
    void testAionEmailAddress() {
        // set up the fixture
        var saEmailAddress = SAEmailAddress.builder()
                .withTag("AION1234");

        // execute the SUT
        var result = saEmailAddress.build().toString();

        // validation
        assertThat(result).isEqualTo("Aion Technology <dev+aion1234@aiontechnology.io>");
    }

    @Test
    void testSAEmailAddress() {
        // set up the fixture
        var saEmailAddress = SAEmailAddress.builder();

        // execute the SUT
        var result = saEmailAddress.build().toString();

        // validation
        assertThat(result).isEqualTo("MentorSuccess™ System Administrator <mentorsuccess@mentorsuccesskids.com>");
    }

    @Test
    void testSAEmailAddressWithOptionalTag() {
        // set up the fixture
        var saEmailAddress = SAEmailAddress.builder()
                .withTag(Optional.of("TAG"));

        // execute the SUT
        var result = saEmailAddress.build().toString();

        // validation
        assertThat(result).isEqualTo("MentorSuccess™ System Administrator <mentorsuccess+tag@mentorsuccesskids.com>");
    }

    @Test
    void testSAEmailAddressWithTag() {
        // set up the fixture
        var saEmailAddress = SAEmailAddress.builder()
                .withTag("TAG");

        // execute the SUT
        var result = saEmailAddress.build().toString();

        // validation
        assertThat(result).isEqualTo("MentorSuccess™ System Administrator <mentorsuccess+tag@mentorsuccesskids.com>");
    }

}
