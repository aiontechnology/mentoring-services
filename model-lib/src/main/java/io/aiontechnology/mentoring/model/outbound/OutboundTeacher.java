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
public class OutboundTeacher<T> extends EntityModel<T> {

    public OutboundTeacher(T content) {
        super(content);
    }

    /** The first name of the teacher. */
    String firstName;

    /** The last name of the teacher. */
    String lastName;

    /** The teacher's email address. */
    String email;

    /** The teacher's work phone number. */
    String workPhone;

    /** The teacher's cell phone */
    String cellPhone;

    /** First grade taught by teacher. */
    Integer grade1;

    /** Second grade taught by teacher. Null if there is only one grade. */
    Integer grade2;

}
