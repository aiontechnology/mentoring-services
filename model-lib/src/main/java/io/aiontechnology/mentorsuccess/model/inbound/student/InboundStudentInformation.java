/*
 * Copyright 2023-2024 Aion Technology LLC
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

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

@Value
@Builder(setterPrefix = "with")
public class InboundStudentInformation implements Serializable {

    Set<String> leadershipSkills;

    Set<String> leadershipTraits;

    Set<String> behaviors;

    String teacherComment;

    @Valid
    InboundStudentAssessment studentAssessment;

}
