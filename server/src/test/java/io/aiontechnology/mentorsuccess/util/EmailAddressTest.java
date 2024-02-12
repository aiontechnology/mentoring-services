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

public class EmailAddressTest {

    @Test
    void testWithEmptyName() {
        // set up the fixture
        var account = "ACCOUNT";
        var domain = "DOMAIN";
        var name = "";

        // execute the SUT
        var emailAddress = EmailAddress.builder(account, domain)
                .withName(name)
                .build();


        // validation
        assertThat(emailAddress.toString()).isEqualTo(account + "@" + domain);
    }

    @Test
    void testWithEmptyOptionalTag() {
        // set up the fixture
        var account = "ACCOUNT";
        var domain = "DOMAIN";
        var tag = "";

        // execute the SUT
        var emailAddress = EmailAddress.builder(account, domain)
                .withTag(Optional.of(tag))
                .build();

        // validation
        assertThat(emailAddress.toString()).isEqualTo(account + "@" + domain);
    }

    @Test
    void testWithEmptyTag() {
        // set up the fixture
        var account = "ACCOUNT";
        var domain = "DOMAIN";
        var tag = "";

        // execute the SUT
        var emailAddress = EmailAddress.builder(account, domain)
                .withTag(tag)
                .build();

        // validation
        assertThat(emailAddress.toString()).isEqualTo(account + "@" + domain);
    }

    @Test
    void testWithNameAndTag() {
        // set up the fixture
        var account = "ACCOUNT";
        var domain = "DOMAIN";
        var name = "NAME";
        var tag = "TAG";

        // execute the SUT
        var emailAddress = EmailAddress.builder(account, domain)
                .withName(name)
                .withTag(tag)
                .build();

        // validation
        assertThat(emailAddress.toString()).isEqualTo(name + " <" + account + "+" + tag + "@" + domain + ">");
    }

    @Test
    void testWithOptionalTag() {
        // set up the fixture
        var account = "ACCOUNT";
        var domain = "DOMAIN";
        var tag = "TAG";

        // execute the SUT
        var emailAddress = EmailAddress.builder(account, domain)
                .withTag(Optional.of(tag))
                .build();

        // validation
        assertThat(emailAddress.toString()).isEqualTo(account + "+" + tag + "@" + domain);
    }

    @Test
    void testWithTag() {
        // set up the fixture
        var account = "ACCOUNT";
        var domain = "DOMAIN";
        var tag = "TAG";

        // execute the SUT
        var emailAddress = EmailAddress.builder(account, domain)
                .withTag(tag)
                .build();

        // validation
        assertThat(emailAddress.toString()).isEqualTo(account + "+" + tag + "@" + domain);
    }

    @Test
    void testWithoutTag() {
        // set up the fixture
        var account = "ACCOUNT";
        var domain = "DOMAIN";

        // execute the SUT
        var emailAddress = EmailAddress.builder(account, domain)
                .build();

        // validation
        assertThat(emailAddress.toString()).isEqualTo(account + "@" + domain);
    }

}
