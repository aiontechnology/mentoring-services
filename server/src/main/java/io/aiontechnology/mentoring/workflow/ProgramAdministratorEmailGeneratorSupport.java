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

package io.aiontechnology.mentoring.workflow;

import io.aiontechnology.mentoring.util.EmailAddress;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.Optional;

public abstract class ProgramAdministratorEmailGeneratorSupport extends EmailGeneratorSupport {

    protected ProgramAdministratorEmailGeneratorSupport(TaskUtilities taskUtilities) {
        super(taskUtilities);
    }

    @Override
    protected String getTo(DelegateExecution execution) {
        TaskUtilities taskUtilities = getTaskUtilities();
        return Optional.ofNullable(taskUtilities.getProgramAdminEmail(execution))
                .map(EmailAddress::builder)
                .map(builder -> builder.withName(taskUtilities.getProgramAdminFullName(execution)))
                .map(EmailAddress.EmailAddressBuilder::build)
                .map(Object::toString)
                .orElseThrow(() -> new IllegalStateException("Unable to find the program administrator's email " +
                        "address"));
    }

}
