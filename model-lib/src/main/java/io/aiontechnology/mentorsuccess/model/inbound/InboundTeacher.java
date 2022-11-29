/*
 * Copyright 2020-2022 Aion Technology LLC
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

import io.aiontechnology.mentorsuccess.model.validation.ValidOrNullPhone;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A model object for teachers.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundTeacher {

    /** The first name of the teacher. */
    @NotNull(message = "{teacher.firstName.notNull}")
    @Size(max = 50, message = "{teacher.firstName.size}")
    String firstName;

    /** The last name of the teacher. */
    @NotNull(message = "{teacher.lastName.notNull}")
    @Size(max = 50, message = "{teacher.lastName.size}")
    String lastName;

    /** The teacher's email address. */
    @Pattern(regexp = "(^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$){1,50}", message = "{teacher.email.invalid}")
    String email;

    /** The teacher's work phone number. */
    @ValidOrNullPhone(message = "{teacher.workPhone.invalid}")
    String workPhone;

    /** The teacher's cell phone */
    @ValidOrNullPhone(message = "{teacher.cellPhone.invalid}")
    String cellPhone;

    /** First grade taught by teacher. */
    @Min(value = 0, message = "{teacher.grade1.invalid}")
    @Max(value = 5, message = "{teacher.grade1.invalid}")
    Integer grade1;

    /** Second grade taught by teacher. Null if there is only one grade. */
    @Min(value = 0, message = "{teacher.grade1.invalid}")
    @Max(value = 5, message = "{teacher.grade1.invalid}")
    Integer grade2;

}
