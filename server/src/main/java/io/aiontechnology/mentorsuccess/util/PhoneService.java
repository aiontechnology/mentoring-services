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

package io.aiontechnology.mentorsuccess.util;

import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * A service for formatting phone numbers.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Component
public class PhoneService {

    /**
     * Format a phone number for the UI. xxxyyyzzzz -{@literal >} (xxx) yyy-zzzz.
     *
     * @param phoneNumber The phone number to format
     * @return The formatted phone number.
     */
    public String format(String phoneNumber) {
        return format(Optional.ofNullable(phoneNumber));
    }

    public String format(Optional<String> phoneNumber) {
        return phoneNumber
                .map(pn -> {
                    var normalized = normalize(pn);
                    if (!normalized.matches("\\d{10}")) {
                        throw new IllegalArgumentException("Unable to normalize phone number: " + pn);
                    }
                    return normalized.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
                })
                .orElse(null);
    }

    /**
     * Normalize a phone number by removing non-numeric symbols. (xxx) yyy-zzzz -{@literal >} xxxyyyzzzz.
     *
     * @param phoneNumber The phone number to normalize.
     * @return The normalized phone number.
     */
    public String normalize(String phoneNumber) {
        return Optional.ofNullable(phoneNumber)
                .map(pn -> pn.replace("(", "")
                        .replace(")", "")
                        .replace("-", "")
                        .replace(" ", ""))
                .orElse(null);
    }

}
