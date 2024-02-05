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

package io.aiontechnology.mentorsuccess.model.inbound.student;

import io.aiontechnology.mentorsuccess.model.enumeration.ContactMethod;
import io.aiontechnology.mentorsuccess.model.validation.Contact;
import io.aiontechnology.mentorsuccess.model.validation.EnumNamePattern;
import io.aiontechnology.mentorsuccess.model.validation.ValidOrNullPhone;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * A model object for the connection between schools and people.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Value
@Builder(setterPrefix = "with")
@Contact
public class InboundContact implements Serializable {

    @Size(max = 50, message = "{contact.label.size}")
    String label;

    /** The contact's first name. */
    @NotNull(message = "{contact.firstName.notNull}")
    @Size(max = 50, message = "{contact.firstName.size}")
    String firstName;

    /** The contact's last name. */
    @NotNull(message = "{contact.lastName.notNull}")
    @Size(max = 50, message = "{contact.lastName.size}")
    String lastName;

    /** The contact's email. */
    @Size(max = 50, message = "{contact.email.size}")
    @Pattern(regexp = "(^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$)", message = "{contact.email.invalid}")
    String email;

    /** The contact's phone. */
    @ValidOrNullPhone(message = "{contact.phone.invalid}")
    String phone;

    @NotNull(message = "{contact.isEmergencyContact.notNull}")
    Boolean isEmergencyContact;

    @EnumNamePattern(regexp = "EITHER|EMAIL|PHONE", message = "{contact.preferredContactMethod.invalid}")
    ContactMethod preferredContactMethod;

    @Size(max = 255, message = "{contact.comment.size}")
    String comment;

}
