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
public class OutboundProgramAdmin<T> extends EntityModel<T> {

    public OutboundProgramAdmin(T content) {
        super(content);
    }

    /** The first name of the program admin. */
    String firstName;

    /** The last name of the program admin. */
    String lastName;

    /** The program admin's email address. */
    String email;

    /** The program admin's work phone number. */
    String workPhone;

    /** The program admin's cell phone. */
    String cellPhone;

}
