/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.entity.workflow;

import io.aiontechnology.mentorsuccess.model.Identifiable;
import lombok.Data;

import java.util.UUID;

@Data
public class StudentRegistration implements Identifiable<UUID> {

    private UUID id;

    private String parent1EmailAddress;

    private String parent1FirstName;

    private String parent1LastName;

    private String studentFirstName;

    private String studentLastName;

}
