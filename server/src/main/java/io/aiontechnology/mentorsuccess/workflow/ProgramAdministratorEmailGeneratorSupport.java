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

package io.aiontechnology.mentorsuccess.workflow;

import io.aiontechnology.mentorsuccess.util.EmailAddress;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProgramAdministratorEmailGeneratorSupport extends EmailGeneratorSupport {

    protected ProgramAdministratorEmailGeneratorSupport(TaskUtilities taskUtilities) {
        super(taskUtilities);
    }

    @Override
    protected String getTo(DelegateExecution execution) {
        TaskUtilities taskUtilities = getTaskUtilities();
        Set<String> paEmailAddresses = taskUtilities.getProgramAdminEmails(execution).stream()
                .map(EmailAddress::builder)
                .map(builder -> builder.withName(taskUtilities.getProgramAdminFullName(execution)))
                .map(EmailAddress.EmailAddressBuilder::build)
                .map(Objects::toString)
                .collect(Collectors.toSet());
        return String.join(",", paEmailAddresses);
    }

}
