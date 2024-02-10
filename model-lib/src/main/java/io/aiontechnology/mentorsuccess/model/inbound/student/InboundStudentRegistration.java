/*
 * Copyright 2022-2024 Aion Technology LLC
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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.net.URI;

import static io.aiontechnology.mentorsuccess.model.inbound.ValidationConstants.GRADE_MAX;
import static io.aiontechnology.mentorsuccess.model.inbound.ValidationConstants.GRADE_MIN;

@Value
@Builder(setterPrefix = "with")
public class InboundStudentRegistration implements Serializable {

    @NotNull(message = "{registration.student.firstname.notNull}")
    @Size(max = 50, message = "{registration.student.firstname.size}")
    String studentFirstName;
    @NotNull(message = "{registration.student.lastname.notNull}")
    @Size(max = 50, message = "{registration.student.lastname.size}")
    String studentLastName;
    @NotNull(message = "{registration.student.grade.notNull}")
    @Min(value = GRADE_MIN, message = "{registration.student.grade.invalid}")
    @Max(value = GRADE_MAX, message = "{registration.student.grade.invalid}")
    Integer grade;
    String parent1FirstName;
    String parent1LastName;
    String parent1PhoneNumber;
    String parent1EmailAddress;
    String parent1PreferredContactMethod;
    String parent2FirstName;
    String parent2LastName;
    String parent2PhoneNumber;
    String parent2EmailAddress;
    String parent2PreferredContactMethod;
    @NotNull(message = "{registration.student.teacher.notNull}")
    URI teacher;
    String preferredSession;
    String emergencyContactFirstName;
    String emergencyContactLastName;
    String emergencyContactPhone;
    String parentSignature;

    public String getStudentFullName() {
        return studentFirstName + " " + studentLastName;
    }

}
