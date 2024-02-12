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

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
public class EmailAddress {

    private final String account;
    private final String domain;
    private final Optional<String> name;
    private final Optional<String> tag;

    public static EmailAddressBuilder builder(String name, String domain) {
        return new EmailAddressBuilder(name, domain);
    }

    public static EmailAddressBuilder builder(String emailAddress) {
        var parts = emailAddress.split("@");
        return new EmailAddressBuilder(parts[0], parts[1]);
    }

    @Override
    public String toString() {
        return name
                .filter(StringUtils::isNotEmpty)
                .map(n -> n + " <" + formatEmailAddress() + ">")
                .orElse(formatEmailAddress());
    }

    private String formatEmailAddress() {
        return account + getTag() + "@" + domain;
    }

    private String getTag() {
        return tag.filter(StringUtils::isNotEmpty)
                .map(t -> "+" + t)
                .orElse("");
    }

    @RequiredArgsConstructor
    public static class EmailAddressBuilder {
        private final String account;
        private final String domain;
        private Optional<String> name = Optional.empty();
        private Optional<String> tag = Optional.empty();

        public EmailAddress build() {
            return new EmailAddress(
                    requireNonNull(account),
                    requireNonNull(domain),
                    name,
                    tag);
        }

        public EmailAddressBuilder withName(String name) {
            this.name = Optional.of(name);
            return this;
        }

        public EmailAddressBuilder withName(Optional<String> name) {
            this.name = name;
            return this;
        }

        public EmailAddressBuilder withTag(String tag) {
            this.tag = Optional.of(tag);
            return this;
        }

        public EmailAddressBuilder withTag(Optional<String> tag) {
            this.tag = tag;
            return this;
        }

    }

}
