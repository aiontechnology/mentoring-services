/*
 * Copyright 2022-2023 Aion Technology LLC
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

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Value
@Builder(setterPrefix = "with")
public class InboundInvitation implements Serializable {

    @NotNull()
    String studentRegistrationUri;

    @NotNull()
    @Size(max = 50)
    String parent1FirstName;

    @NotNull()
    @Size(max = 50)
    String parent1LastName;

    @NotNull(message = "{registration.parent1.email.notNull}")
    @Size(max = 50, message = "{registration.parent1.email.size}")
    @Pattern(regexp = "(^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$)", message = "{registration.parent1.email.invalid}")
    String parent1EmailAddress;

    @NotNull(message = "{registration.student.firstname.notNull}")
    @Size(max = 50, message = "{registration.student.firstname.size}")
    String studentFirstName;

    @NotNull(message = "{registration.student.lastname.notNull}")
    @Size(max = 50, message = "{registration.student.lastname.size}")
    String studentLastName;

    public String getStudentFullName() {
        return studentFirstName + " " + studentLastName;
    }

}
