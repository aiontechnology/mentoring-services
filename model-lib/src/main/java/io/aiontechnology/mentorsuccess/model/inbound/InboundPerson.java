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

package io.aiontechnology.mentorsuccess.model.inbound;

import io.aiontechnology.mentorsuccess.model.validation.ValidOrNullPhone;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * A model object for people.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundPerson {

    /** The person's id. */
    UUID id;

    /** The person's first name. */
    @NotNull(message = "{person.firstName.notNull}")
    @Size(max = 50, message = "{person.firstName.size}")
    String firstName;

    /** The person's last name. */
    @NotNull(message = "{person.lastName.notNull}")
    @Size(max = 50, message = "{person.lastName.size}")
    String lastName;

    /** The person's email. */
    @Pattern(regexp = "(^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$){1,50}", message = "{person.email.invalid}")
    String email;

    /** The person's work phone number. */
    @ValidOrNullPhone(message = "{person.workPhone.invalid}")
    String workPhone;

    /** The person's cell phone number. */
    @ValidOrNullPhone(message = "{person.cellPhone.invalid}")
    String cellPhone;

}
