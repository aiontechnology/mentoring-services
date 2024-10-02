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

import io.aiontechnology.mentoring.model.enumeration.RoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

/**
 * @author Whitney Hunter
 * @since 0.4.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OutboundPersonnel<T> extends EntityModel<T> {

    public OutboundPersonnel(T content) {
        super(content);
    }

    /** The personnel type */
    RoleType type;

    /** The first name. */
    String firstName;

    /** The last name. */
    String lastName;

    /** The email address. */
    String email;

    /** The work phone number. */
    String workPhone;

    /** The cell phone number. */
    String cellPhone;

}
