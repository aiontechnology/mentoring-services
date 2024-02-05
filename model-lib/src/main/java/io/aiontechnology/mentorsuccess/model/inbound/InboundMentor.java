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

import io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation;
import io.aiontechnology.mentorsuccess.model.validation.ValidOrNullPhone;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

/**
 * A model object for mentors.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundMentor {

    /** The first name of the mentor. */
    @NotNull(message = "{mentor.firstName.notNull}")
    @Size(max = 50, message = "{mentor.firstName.size}")
    String firstName;

    /** The last name of the mentor. */
    @NotNull(message = "{mentor.lastName.notNull}")
    @Size(max = 50, message = "{mentor.lastName.size}")
    String lastName;

    /** The mentor's email address. */
    @Pattern(regexp = "(^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$){1,50}", message = "{mentor.email.invalid}")
    String email;

    /** The mentor's work phone number. */
    @ValidOrNullPhone(message = "{mentor.workPhone.invalid}")
    String workPhone;

    /** The mentor's cell phone */
    @ValidOrNullPhone(message = "{mentor.cellPhone.invalid}")
    String cellPhone;

    /** The mentor's availability */
    @Size(max = 100, message = "{mentor.availability.size}")
    String availability;

    @NotNull(message = "{mentor.location.notNull}")
    ResourceLocation location;

    @NotNull(message = "{mentor.mediaRelease.notNull}")
    Boolean mediaReleaseSigned;

    /** Has the mentor had a bockground check completed? */
    @NotNull(message = "{mentor.backgroundCheckCompleted.notNull}")
    Boolean backgroundCheckCompleted;

}
