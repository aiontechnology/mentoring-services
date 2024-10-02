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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class EmailList {

    private Set<String> emailAddresses;

    private EmailList(Set<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public static EmailListBuilder builder() {
        return new EmailListBuilder(null, null);
    }

    public static EmailListBuilder builder(String saName, String saDomain) {
        return new EmailListBuilder(requireNonNull(saName), requireNonNull(saDomain));
    }

    @Override
    public String toString() {
        var sortedEmailAddresses = new ArrayList<>(emailAddresses);
        Collections.sort(sortedEmailAddresses);
        return join(sortedEmailAddresses, ", ");
    }

    public static class EmailListBuilder {
        private List<String> emailAddresses = new ArrayList<>();
        private String saDomain;
        private String saName;
        private String saTag;

        private EmailListBuilder(String saName, String saDomain) {
            this.saName = saName;
            this.saDomain = saDomain;
        }

        public EmailList build() {
            Set<String> emailAddresses = new HashSet<>(this.emailAddresses);
            if (isNotEmpty(saDomain) && isNotEmpty(saName)) {
                emailAddresses.add(
                        isNotEmpty(saTag)
                                ? saName + "+" + saTag + "@" + saDomain
                                : saName + "@" + saDomain
                );
            }
            return new EmailList(emailAddresses);
        }

        public EmailListBuilder withEmailAddress(String emailAddress) {
            this.emailAddresses.add(emailAddress);
            return this;
        }

        public EmailListBuilder withSATag(String SATag) {
            this.saTag = SATag;
            return this;
        }

    }

}
