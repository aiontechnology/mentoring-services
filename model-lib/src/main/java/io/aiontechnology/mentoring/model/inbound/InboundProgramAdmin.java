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

package io.aiontechnology.mentoring.model.inbound;

import io.aiontechnology.mentoring.model.validation.ValidOrNullPhone;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

/**
 * A model object for phonograms.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundProgramAdmin {

    /** The first name of the program admin. */
    @NotNull(message = "{programAdmin.firstName.notNull}")
    @Size(max = 50, message = "{programAdmin.firstName.size}")
    String firstName;

    /** The last name of the program admin. */
    @NotNull(message = "{programAdmin.lastName.notNull}")
    @Size(max = 50, message = "{programAdmin.lastName.size}")
    String lastName;

    /** The program admin's email address. */
    @NotNull(message = "{programAdmin.email.notNull}")
    @Size(max = 50, message = "{programAdmin.email.size}")
    @Pattern(regexp = "(^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$)", message = "{programAdmin.email.invalid}")
    String email;

    /** The program admin's work phone number. */
    @ValidOrNullPhone(message = "{programAdmin.workPhone.invalid}")
    String workPhone;

    /** The program admin's cell phone. */
    @ValidOrNullPhone(message = "{programAdmin.cellPhone.invalid}")
    String cellPhone;

}
