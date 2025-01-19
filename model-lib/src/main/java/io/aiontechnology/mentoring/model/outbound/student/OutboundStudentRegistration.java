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

package io.aiontechnology.mentoring.model.outbound.student;

import io.aiontechnology.mentoring.model.outbound.OutboundProgramAdmin;
import io.aiontechnology.mentoring.model.outbound.OutboundSchool;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OutboundStudentRegistration<T> extends EntityModel<T> {

    String studentFirstName;

    String studentLastName;

    String parent1FirstName;

    String parent1LastName;

    String parent1EmailAddress;

    OutboundSchool school;

    OutboundProgramAdmin programAdmin;

    Collection teachers;

    public OutboundStudentRegistration(T content) {
        super(content);
    }

}
