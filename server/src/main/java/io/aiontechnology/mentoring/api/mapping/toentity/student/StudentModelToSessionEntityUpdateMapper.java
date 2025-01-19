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

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayToCollectionUpdateMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.StudentActivityFocus;
import io.aiontechnology.mentoring.entity.StudentBehavior;
import io.aiontechnology.mentoring.entity.StudentLeadershipSkill;
import io.aiontechnology.mentoring.entity.StudentLeadershipTrait;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentActivityFocus;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentBehavior;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipSkill;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipTrait;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Map an {@link InboundStudent} to a {@link StudentSchoolSession}.
 *
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Component
@RequiredArgsConstructor
public class StudentModelToSessionEntityUpdateMapper
        implements OneWayUpdateMapper<InboundStudent, StudentSchoolSession> {

    // Mappers
    private final OneWayCollectionMapper<String, Interest> interestModelToEntityMapper;

    private final OneWayMapper<URI, SchoolPersonRole> mentorModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentActivityFocus, StudentActivityFocus> studentActivityFocusModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentBehavior, StudentBehavior> studentBehaviorModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentLeadershipSkill, StudentLeadershipSkill> studentLeadershipSkillModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentLeadershipTrait, StudentLeadershipTrait> studentLeadershipTraitModelToEntityMapper;

    private final OneWayMapper<URI, SchoolPersonRole> teacherModelToEntityMapper;

    @Override
    public Optional<StudentSchoolSession> map(InboundStudent inboundStudent,
            StudentSchoolSession studentSchoolSession) {
        requireNonNull(inboundStudent);
        return Optional.ofNullable(studentSchoolSession)
                .map(s -> mapProperties(inboundStudent, s))
                .map(s -> mapInterests(inboundStudent, s))
                .map(s -> mapBehaviors(inboundStudent, s))
                .map(s -> mapMentor(inboundStudent, s))
                .map(s -> mapActivityFocuses(inboundStudent, s))
                .map(s -> mapLeadershipSkills(inboundStudent, s))
                .map(s -> mapLeadershipTraits(inboundStudent, s))
                .map(s -> mapTeacher(inboundStudent, s));
    }

    private StudentSchoolSession mapActivityFocuses(InboundStudent inboundStudent,
            StudentSchoolSession studentSchoolSession) {
        Optional.ofNullable(studentActivityFocusModelToEntityMapper)
                .ifPresent(mapper -> {
                    Optional.of(inboundStudent)
                            .map(InboundStudent::getActivityFocuses)
                            .map(activityFocusModels -> InboundStudentActivityFocus.builder()
                                    .withActivityFocuses(activityFocusModels)
                                    .withTeacher(inboundStudent.getTeacher().getUri())
                                    .build())
                            .map(activityFocusModel -> mapper.map(activityFocusModel,
                                    studentSchoolSession.getStudentActivityFocuses()))
                            .ifPresent(studentSchoolSession::setStudentActivityFocuses);
                });
        return studentSchoolSession;
    }

    private StudentSchoolSession mapBehaviors(InboundStudent inboundStudent,
            StudentSchoolSession studentSchoolSession) {
        Optional.ofNullable(studentBehaviorModelToEntityMapper)
                .ifPresent(mapper -> {
                    Optional.of(inboundStudent)
                            .map(InboundStudent::getBehaviors)
                            .map(behaviors -> InboundStudentBehavior.builder()
                                    .withBehaviors(behaviors)
                                    .withTeacher(inboundStudent.getTeacher().getUri())
                                    .build())
                            .map(behaviorModel -> mapper.map(behaviorModel, studentSchoolSession.getStudentBehaviors()))
                            .ifPresent(studentSchoolSession::setStudentBehaviors);
                });
        return studentSchoolSession;
    }

    private StudentSchoolSession mapInterests(InboundStudent inboundStudent,
            StudentSchoolSession studentSchoolSession) {
        Optional.ofNullable(interestModelToEntityMapper)
                .flatMap(mapper -> mapper.map(inboundStudent.getInterests()))
                .ifPresent(studentSchoolSession::setInterests);
        return studentSchoolSession;
    }

    private StudentSchoolSession mapLeadershipSkills(InboundStudent inboundStudent,
            StudentSchoolSession studentSchoolSession) {
        Optional.ofNullable(studentLeadershipSkillModelToEntityMapper)
                .ifPresent(mapper -> {
                    Optional.of(inboundStudent)
                            .map(InboundStudent::getLeadershipSkills)
                            .map(leadershipSkillModels -> InboundStudentLeadershipSkill.builder()
                                    .withLeadershipSkills(leadershipSkillModels)
                                    .withTeacher(inboundStudent.getTeacher().getUri())
                                    .build())
                            .map(leadershipSkillModel -> mapper.map(leadershipSkillModel,
                                    studentSchoolSession.getStudentLeadershipSkills()))
                            .ifPresent(studentSchoolSession::setStudentLeadershipSkills);
                });
        return studentSchoolSession;
    }

    private StudentSchoolSession mapLeadershipTraits(InboundStudent inboundStudent,
            StudentSchoolSession studentSchoolSession) {
        Optional.ofNullable(studentLeadershipTraitModelToEntityMapper)
                .ifPresent(mapper -> {
                    Optional.of(inboundStudent)
                            .map(InboundStudent::getLeadershipTraits)
                            .map(leadershipTraitModels -> InboundStudentLeadershipTrait.builder()
                                    .withLeadershipTraits(leadershipTraitModels)
                                    .withTeacher(inboundStudent.getTeacher().getUri())
                                    .build())
                            .map(leadershipTraitModel -> mapper.map(leadershipTraitModel,
                                    studentSchoolSession.getStudentLeadershipTraits()))
                            .ifPresent(studentSchoolSession::setStudentLeadershipTraits);
                });
        return studentSchoolSession;
    }

    private StudentSchoolSession mapMentor(InboundStudent inboundStudent, StudentSchoolSession studentSchoolSession) {
        Optional.ofNullable(inboundStudent.getMentor())
                .ifPresent(inboundMentor -> {
                    Optional<SchoolPersonRole> requestedRole =
                            mentorModelToEntityMapper.map(inboundMentor.getUri());
                    studentSchoolSession.setMentor(requestedRole
                            .orElseThrow(() -> new NotFoundException("Unable to find specified mentor")));
                });
        return studentSchoolSession;
    }

    private StudentSchoolSession mapProperties(InboundStudent inboundStudent,
            StudentSchoolSession studentSchoolSession) {
        studentSchoolSession.setGrade(inboundStudent.getGrade());
        studentSchoolSession.setPreferredTime(inboundStudent.getPreferredTime());
        studentSchoolSession.setActualTime(inboundStudent.getActualTime());
        studentSchoolSession.setStartDate(inboundStudent.getStartDate());
        studentSchoolSession.setLocation(inboundStudent.getLocation());
        studentSchoolSession.setIsRegistrationSigned(inboundStudent.getRegistrationSigned());
        studentSchoolSession.setIsMediaReleaseSigned(inboundStudent.getMediaReleaseSigned());
        studentSchoolSession.setPreBehavioralAssessment(inboundStudent.getPreBehavioralAssessment());
        studentSchoolSession.setPostBehavioralAssessment(inboundStudent.getPostBehavioralAssessment());
        studentSchoolSession.setIsActive(true);
        return studentSchoolSession;
    }

    private StudentSchoolSession mapTeacher(InboundStudent inboundStudent, StudentSchoolSession studentSchoolSession) {
        Optional.ofNullable(inboundStudent.getTeacher())
                .ifPresent(inboundTeacher -> {
                    SchoolPersonRole teacher =
                            (inboundTeacher.getUri() == null || inboundTeacher.getUri().toString().equals(""))
                                    ? null
                                    : teacherModelToEntityMapper.map(inboundTeacher.getUri())
                                    .orElseThrow(() -> new NotFoundException("Unable to find specified teacher"));
                    studentSchoolSession.setTeacher(teacher);
                    studentSchoolSession.setTeacherComment(inboundTeacher.getComment());
                });
        return studentSchoolSession;
    }

}
