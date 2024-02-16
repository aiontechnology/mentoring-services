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

import java.util.Optional;

public class SAEmailAddress extends EmailAddress {

    private SAEmailAddress(String account, String domain, Optional<String> name, Optional<String> tag) {
        super(account, domain, name, tag);
    }

    public static SAEmailAddressBuilder builder() {
        return new SAEmailAddressBuilder();
    }

    public static class SAEmailAddressBuilder {
        private final String AION_TAG = "aion";
        private final String AION_DOMAIN = "aiontechnology.io";
        private final String AION_ACCOUNT = "dev";
        private final String AION_NAME = "Aion Technology";
        private final String SA_DOMAIN = "hisheartfoundation.org";
        private final String SA_ACCOUNT = "mentorsuccess";
        private final String SA_NAME = "MentorSuccess™ System Administrator";
        private Optional<String> tag = Optional.empty();

        public SAEmailAddress build() {
            var lower = tag.map(String::toLowerCase);
            return lower.filter(t -> t.startsWith(AION_TAG))
                    .map(t -> new SAEmailAddress(AION_ACCOUNT, AION_DOMAIN, Optional.of(AION_NAME), lower))
                    .orElse(new SAEmailAddress(SA_ACCOUNT, SA_DOMAIN, Optional.of(SA_NAME), lower));
        }

        public SAEmailAddressBuilder withTag(String tag) {
            this.tag = Optional.of(tag);
            return this;
        }

        public SAEmailAddressBuilder withTag(Optional<String> tag) {
            this.tag = tag;
            return this;
        }

    }

}
