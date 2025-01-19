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

package io.aiontechnology.mentoring.model.outbound;

import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.UUID;

/**
 * Model object representing a mentor to be returned to a client.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OutboundMentor<T> extends EntityModel<T> {

    public OutboundMentor(T content) {
        super(content);
    }

    /** The mentor's id. */
    UUID id;

    /** The first name of the mentor. */
    String firstName;

    /** The last name of the mentor. */
    String lastName;

    /** The mentor's email address. */
    String email;

    /** The mentor's work phone number. */
    String workPhone;

    /** The mentor's cell phone */
    String cellPhone;

    /** The mentor's availability */
    String availability;

    /** The mentor's location (online, offline or both) */
    ResourceLocation location;

    /** Has the mentor signed the media release? */
    Boolean mediaReleaseSigned;

    /** Has the mentor had a bockground check completed? */
    Boolean backgroundCheckCompleted;

}
