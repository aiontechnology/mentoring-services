/*
 * Copyright 2020-2023 Aion Technology LLC
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OutboundStudent<T> extends EntityModel<T> {

    public OutboundStudent(T content) {
        super(content);
    }

    UUID id;

    String studentId;

    String firstName;

    String lastName;

    Integer grade;

    String preferredTime;

    String actualTime;

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    Date startDate;

    ResourceLocation location;

    Boolean registrationSigned;

    Boolean mediaReleaseSigned;

    Boolean parentSigned;

    Integer preBehavioralAssessment;

    Integer postBehavioralAssessment;

    Boolean teacherInfoWorkflowAllowed;

    OutboundStudentTeacher teacher;

    OutboundStudentMentor mentor;

    Collection<String> activityFocuses;

    Collection<String> interests;

    Collection<String> behaviors;

    Collection<String> leadershipSkills;

    Collection<String> leadershipTraits;

    Collection<OutboundContact> contacts;

}
