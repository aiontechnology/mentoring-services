/*
 * Copyright 2021 Aion Technology LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.aiontechnology.mentoring.util;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
public class SchoolUUIDMatcherTest {

    @Test
    void testMatchesNoExtention() throws Exception {
        // setup the fixture
        UUID uuid = UUID.randomUUID();
        URI uri = URI.create("/api/v1/schools/" + uuid);

        SchoolUUIDMatcher schoolUUIDMatcher = new SchoolUUIDMatcher();

        // execute the SUT
        boolean result = schoolUUIDMatcher.match(uri, uuid);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testMatchesWithExtension() throws Exception {
        // setup the fixture
        UUID uuid = UUID.randomUUID();
        URI uri = URI.create("/api/v1/schools/" + uuid + "/teachers");

        SchoolUUIDMatcher schoolUUIDMatcher = new SchoolUUIDMatcher();

        // execute the SUT
        boolean result = schoolUUIDMatcher.match(uri, uuid);

        // validation
        assertThat(result).isTrue();
    }

    @Test
    void testDoesNotMatchNoExtension() throws Exception {
        // setup the fixture
        UUID uuid = UUID.randomUUID();
        URI uri = URI.create("/api/v1/schools/" + uuid);

        SchoolUUIDMatcher schoolUUIDMatcher = new SchoolUUIDMatcher();

        // execute the SUT
        boolean result = schoolUUIDMatcher.match(uri, UUID.randomUUID());

        // validation
        assertThat(result).isFalse();
    }

    @Test
    void testDoesNotMatchWithExtension() throws Exception {
        // setup the fixture
        UUID uuid = UUID.randomUUID();
        URI uri = URI.create("/api/v1/schools/" + uuid + "/teachers");

        SchoolUUIDMatcher schoolUUIDMatcher = new SchoolUUIDMatcher();

        // execute the SUT
        boolean result = schoolUUIDMatcher.match(uri, UUID.randomUUID());

        // validation
        assertThat(result).isFalse();
    }

    @Test
    void testDoesNotMatchDifferentPrefix() throws Exception {
        // setup the fixture
        UUID uuid = UUID.randomUUID();
        URI uri = URI.create("/api/v1/other/" + uuid);

        SchoolUUIDMatcher schoolUUIDMatcher = new SchoolUUIDMatcher();

        // execute the SUT
        boolean result = schoolUUIDMatcher.match(uri, uuid);

        // validation
        assertThat(result).isFalse();
    }

}
