/*
 * Copyright 2020-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.model.outbound.student;

import io.aiontechnology.mentoring.model.enumeration.ContactMethod;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Model object representing a contact to be returned to a client.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Value
@EqualsAndHashCode
@Builder(setterPrefix = "with")
public class OutboundContact {

    String label;

    /** The contact's first name. */
    String firstName;

    /** The contact's last name. */
    String lastName;

    /** The contact's email. */
    String email;

    /** The contact's phone number. */
    String phone;

    Boolean isEmergencyContact;

    ContactMethod preferredContactMethod;

    String comment;

}
