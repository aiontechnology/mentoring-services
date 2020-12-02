/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentorsuccess.model.inbound;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "{mentor.workPhone.invalid}")
    String workPhone;

    /** The mentor's cell phone */
    @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "{mentor.cellPhone.invalid}")
    String cellPhone;

    /** The mentor's availability */
    @Size(max = 100, message = "{mentor.availability.size}")
    String availability;

}