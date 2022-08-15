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

package io.aiontechnology.mentorsuccess.api.mapping.tomodel.student;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentorsuccess.entity.StudentBehavior;
import io.aiontechnology.mentorsuccess.entity.StudentLeadershipSkill;
import io.aiontechnology.mentorsuccess.entity.StudentLeadershipTrait;
import io.aiontechnology.mentorsuccess.entity.StudentMentor;
import io.aiontechnology.mentorsuccess.entity.StudentSchoolSession;
import io.aiontechnology.mentorsuccess.entity.reference.Interest;
import io.aiontechnology.mentorsuccess.model.outbound.student.OutboundStudentMentor;
import io.aiontechnology.mentorsuccess.resource.StudentResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Component
@RequiredArgsConstructor
public class StudentSchoolSessionEntityToModelUpdateMapper
        implements OneWayUpdateMapper<StudentSchoolSession, StudentResource> {

    // Mappers
    private final OneWayCollectionMapper<Interest, String> interestModelToEntityMapper;
    private final OneWayCollectionMapper<StudentBehavior, String> studentBehaviorEntityToModelMapper;
    private final OneWayCollectionMapper<StudentLeadershipSkill, String> studentLeadershipSkillEntityToModelMapper;
    private final OneWayCollectionMapper<StudentLeadershipTrait, String> studentLeadershipTraitEntityToModelMapper;
//    private final OneWayMapper<StudentTeacher, OutboundStudentTeacher> studentTeacherEntityToModelMapper;
    private final OneWayMapper<StudentMentor, OutboundStudentMentor> studentMentorEntityToModelMapper;

    @Override
    public Optional<StudentResource> map(StudentSchoolSession studentSchoolSession, StudentResource resource) {
        requireNonNull(studentSchoolSession);
        return Optional.ofNullable(resource)
                .map(s -> {
                    s.setGrade(studentSchoolSession.getGrade());
                    s.setPreferredTime(studentSchoolSession.getPreferredTime());
                    s.setActualTime(studentSchoolSession.getActualTime());
                    s.setLocation(studentSchoolSession.getLocation());
                    s.setMediaReleaseSigned(studentSchoolSession.getIsMediaReleaseSigned());
                    s.setStartDate(studentSchoolSession.getStartDate());
                    s.setInterests(interestModelToEntityMapper.map(studentSchoolSession.getInterests()).orElse(Collections.emptyList()));
                    s.setBehaviors(studentBehaviorEntityToModelMapper.map(studentSchoolSession.getStudentBehaviors()).orElse(Collections.emptyList()));
                    s.setLeadershipSkills(studentLeadershipSkillEntityToModelMapper.map(studentSchoolSession.getStudentLeadershipSkills()).orElse(Collections.emptyList()));
                    s.setLeadershipTraits(studentLeadershipTraitEntityToModelMapper.map(studentSchoolSession.getStudentLeadershipTraits()).orElse(Collections.emptyList()));
//                    s.setTeacher(studentTeacherEntityToModelMapper.map(studentSchoolSession.getTeacher()).orElse(null));
//                    s.setMentor(studentMentorEntityToModelMapper.map(studentSchoolSession.getMentor()).orElse(null));
                    return s;
                });
    }

}
