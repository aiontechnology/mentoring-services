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

class EmailListTest {

    @Test
    void testEmptyTag() {
        // set up the fixture
        var emailList = EmailList.builder("sa", "example.com")
                .withSATag("")
                .build();

        // execute the SUT
        String result = emailList.toString();

        // validation
        assertThat(result).isEqualTo("sa@example.com");
    }

    @Test
    void testNoEmailsWithSANoTag() {
        // set up the fixture
        var emailList = EmailList.builder("sa", "example.com")
                .build();

        // execute the SUT
        String result = emailList.toString();

        // validation
        assertThat(result).isEqualTo("sa@example.com");
    }

    @Test
    void testNoEmailsWithSAWithTag() {
        // set up the fixture
        var emailList = EmailList.builder("sa", "example.com")
                .withSATag("tag")
                .build();

        // execute the SUT
        String result = emailList.toString();

        // validation
        assertThat(result).isEqualTo("sa+tag@example.com");
    }

    @Test
    void testOneEmailNoSA() {
        // set up the fixture
        var emailList = EmailList.builder()
                .withEmailAddress("test@example.com")
                .build();

        // execute the SUT
        String result = emailList.toString();

        // validation
        assertThat(result).isEqualTo("test@example.com");
    }

    @Test
    void testOneEmailWithSANoTag() {
        // set up the fixture
        var emailList = EmailList.builder("sa", "example.com")
                .withEmailAddress("test@example.com")
                .build();

        // execute the SUT
        String result = emailList.toString();

        // validation
        assertThat(result).isEqualTo("sa@example.com, test@example.com");
    }

    @Test
    void testOneEmailsWithSAWithTag() {
        // set up the fixture
        var emailList = EmailList.builder("sa", "example.com")
                .withEmailAddress("test@example.com")
                .withSATag("tag")
                .build();

        // execute the SUT
        String result = emailList.toString();

        // validation
        assertThat(result).isEqualTo("sa+tag@example.com, test@example.com");
    }

    @Test
    void testTwoEmailsNoSA() {
        // set up the fixture
        var emailList = EmailList.builder()
                .withEmailAddress("test1@example.com")
                .withEmailAddress("test2@example.com")
                .build();

        // execute the SUT
        String result = emailList.toString();

        // validation
        assertThat(result).isEqualTo("test1@example.com, test2@example.com");
    }

}
