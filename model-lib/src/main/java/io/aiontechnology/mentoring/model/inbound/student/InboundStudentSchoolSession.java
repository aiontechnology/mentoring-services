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

package io.aiontechnology.mentoring.model.inbound.student;

import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import io.aiontechnology.mentoring.model.validation.EnumNamePattern;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.net.URI;
import java.util.Date;
import java.util.Set;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundStudentSchoolSession {

    @NotNull(message = "{studentsession.student.notNull}")
    URI student;

    @NotNull(message = "{studentsession.grade.notNull}")
    @Min(value = 0, message = "{studentsession.grade.invalid}")
    @Max(value = 5, message = "{studentsession.grade.invalid}")
    Integer grade;

    Integer preBehavioralAssessment;

    Integer postBehavioralAssessment;

    @Size(max = 30, message = "{studentsession.preferredTime.size}")
    String preferredTime;

    @Size(max = 30, message = "{studentsession.actualTime.size}")
    String actualTime;

    Date startDate;

    @NotNull(message = "{studentsession.location.notNull}")
    @EnumNamePattern(regexp = "ONLINE|OFFLINE|BOTH", message = "{student.location.invalid}")
    ResourceLocation location;

    @NotNull(message = "{studentsession.registrationSigned.notNull")
    Boolean registrationSigned;

    @NotNull(message = "{studentsession.mediaRelease.notNull}")
    Boolean mediaReleaseSigned;

    @NotNull(message = "{studentsession.teacher.notNull}")
    @Valid
    InboundStudentTeacher teacher;

    @Valid
    InboundStudentMentor mentor;

    Set<String> activityFocuses;

    Set<String> interests;

    Set<String> behaviors;

    Set<String> leadershipSkills;

    Set<String> leadershipTraits;

}
