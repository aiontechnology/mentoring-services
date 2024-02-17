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

package io.aiontechnology.mentorsuccess.model.outbound;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.UUID;

/**
 * @author Whitney Hunter
 * @since 0.4.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OutboundSchool<T> extends EntityModel<T> {

    public OutboundSchool(T content) {
        super(content);
    }

    /** The school's id */
    UUID id;

    /** The name of the school */
    String name;

    /** The school's address */
    OutboundAddress address;

    /** The school's phone number */
    String phone;

    /** The school district that the school is in */
    String district;

    /** Indicates whether the school is private or public */
    Boolean isPrivate;

    /** The current session for the school */
    OutboundSchoolSession currentSession;

    /** A tag that is placed in all emails to mentorsuccess (e.g. mentorsuccess+tag@hisheartfoundation.org) */
    String emailTag;

}
