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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collection;
import java.util.Date;

/**
 * Value object for outbound school sessions.
 *
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Value
@EqualsAndHashCode
@Builder(setterPrefix = "with")
public class OutboundStudentSchoolSession {

    Integer grade;

    String preferredTime;

    String actualTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date startDate;

    ResourceLocation location;

    Boolean registrationSigned;

    Boolean mediaReleaseSigned;

    OutboundStudentTeacher teacher;

    OutboundStudentMentor mentor;

    Collection<String> activityFocuses;

    Collection<String> interests;

    Collection<String> behaviors;

    Collection<String> leadershipSkills;

    Collection<String> leadershipTraits;

}
