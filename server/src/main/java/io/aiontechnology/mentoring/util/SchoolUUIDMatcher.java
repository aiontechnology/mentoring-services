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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@Component
@Slf4j
public class SchoolUUIDMatcher {

    private final Pattern SCHOOL_PATTERN =
            Pattern.compile("/api/v1/schools/([0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12})");

    public boolean match(URI uri, UUID schoolId) {
        boolean isMatching = false;
        Matcher matcher = SCHOOL_PATTERN.matcher(uri.toString());
        if (matcher.find()) {
            UUID matched = UUID.fromString(matcher.group(1));
            log.debug("==> School in URI: {}", matched);
            isMatching = schoolId.equals(matched);
        }
        return isMatching;
    }

}
