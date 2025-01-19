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
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentActivityFocus;
import io.aiontechnology.mentoring.entity.StudentBehavior;
import io.aiontechnology.mentoring.entity.StudentLeadershipSkill;
import io.aiontechnology.mentoring.entity.StudentLeadershipTrait;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentActivityFocus;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentBehavior;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipSkill;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipTrait;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentSchoolSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Component
@RequiredArgsConstructor
public class StudentSchoolSessionModelToEntityUpdateMapper implements
        OneWayUpdateMapper<InboundStudentSchoolSession, StudentSchoolSession> {

    private final OneWayCollectionMapper<String, Interest> interestModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentActivityFocus, StudentActivityFocus> studentActivityFocusModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentBehavior, StudentBehavior> studentBehaviorModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentLeadershipSkill, StudentLeadershipSkill> studentLeadershipSkillModelToEntityMapper;

    private final OneWayToCollectionUpdateMapper<InboundStudentLeadershipTrait, StudentLeadershipTrait> studentLeadershipTraitModelToEntityMapper;

    private final OneWayMapper<URI, Student> studentUriToEntityMapper;

//    private final OneWayUpdateMapper<InboundStudentTeacher, StudentTeacher> studentTeacherModelToEntityMapper;

//    private final OneWayUpdateMapper<InboundStudentMentor, StudentMentor> studentMentorModelToEntityMapper;

    /**
     * Map the set of behavior strings in the {@link InboundStudentSchoolSession} into a new
     * {@link InboundStudentBehavior} assuming that the behaviors were provided by the student's teacher.
     */
    private final OneWayMapper<InboundStudentSchoolSession, InboundStudentBehavior> studentBehaviorModelMapper =
            inboundStudentSchoolSession -> Optional.of(inboundStudentSchoolSession)
                    .map(InboundStudentSchoolSession::getBehaviors)
                    .map(behaviorModels -> InboundStudentBehavior.builder()
                            .withBehaviors(behaviorModels)
                            .withTeacher(inboundStudentSchoolSession.getTeacher().getUri())
                            .build());

    private final OneWayMapper<InboundStudentSchoolSession, InboundStudentActivityFocus> studentActivityFocusModelMapper =
            inboundStudentSchoolSession -> Optional.of(inboundStudentSchoolSession)
                    .map(InboundStudentSchoolSession::getActivityFocuses)
                    .map(activityFocusModels -> InboundStudentActivityFocus.builder()
                            .withActivityFocuses(activityFocusModels)
                            .withTeacher(inboundStudentSchoolSession.getTeacher().getUri())
                            .build());

    /**
     * Map the set of leadership skill strings in the {@link InboundStudentSchoolSession} into a new
     * {@link InboundStudentLeadershipSkill} assuming that the skills were provided by the student's teacher.
     */
    private final OneWayMapper<InboundStudentSchoolSession, InboundStudentLeadershipSkill> studentLeadershipSkillModelMapper =
            inboundStudentSchoolSession -> Optional.of(inboundStudentSchoolSession)
                    .map(InboundStudentSchoolSession::getLeadershipSkills)
                    .map(leadershipSkillModels -> InboundStudentLeadershipSkill.builder()
                            .withLeadershipSkills(leadershipSkillModels)
                            .withTeacher(inboundStudentSchoolSession.getTeacher().getUri())
                            .build());

    /**
     * Map the set of leadership trait strings in the {@link InboundStudentSchoolSession} into a new
     * {@link InboundStudentLeadershipTrait} assuming that the skills were provided by the student's teacher.
     */
    private final OneWayMapper<InboundStudentSchoolSession, InboundStudentLeadershipTrait> studentLeadershipTraitModelMapper =
            inboundStudentSchoolSession -> Optional.of(inboundStudentSchoolSession)
                    .map(InboundStudentSchoolSession::getLeadershipTraits)
                    .map(leadershipTraitModels -> InboundStudentLeadershipTrait.builder()
                            .withLeadershipTraits(leadershipTraitModels)
                            .withTeacher(inboundStudentSchoolSession.getTeacher().getUri())
                            .build());

    @Override
    public Optional<StudentSchoolSession> map(InboundStudentSchoolSession inboundStudentSchoolSession,
            StudentSchoolSession studentSchoolSession) {
        return Optional.ofNullable(inboundStudentSchoolSession)
                .map(ss -> {
                    studentUriToEntityMapper.map(ss.getStudent())
                            .ifPresent(studentSchoolSession::setStudent);
                    studentSchoolSession.setGrade(ss.getGrade());
                    studentSchoolSession.setPreferredTime(ss.getPreferredTime());
                    studentSchoolSession.setActualTime(ss.getActualTime());
                    studentSchoolSession.setStartDate(ss.getStartDate());
                    studentSchoolSession.setLocation(ss.getLocation());
                    studentSchoolSession.setIsRegistrationSigned(ss.getRegistrationSigned());
                    studentSchoolSession.setIsMediaReleaseSigned(ss.getMediaReleaseSigned());
                    studentSchoolSession.setPreBehavioralAssessment(ss.getPreBehavioralAssessment());
                    studentSchoolSession.setPostBehavioralAssessment(ss.getPostBehavioralAssessment());
                    studentSchoolSession.setStudentInterests(interestModelToEntityMapper.map(ss.getInterests()).orElse(Collections.emptyList()));
                    Collection<StudentActivityFocus> activityFocuses = Optional.of(studentSchoolSession)
                            .map(sy -> sy.getStudentActivityFocuses()).orElse(Collections.EMPTY_LIST);
                    studentActivityFocusModelMapper
                            .map(ss)
                            .ifPresent(inboundStudentActivityFocusModel ->
                                    studentSchoolSession.setStudentActivityFocuses(studentActivityFocusModelToEntityMapper
                                            .map(inboundStudentActivityFocusModel, activityFocuses)));
                    Collection<StudentBehavior> studentBehaviors = Optional.of(studentSchoolSession)
                            .map(sy -> sy.getStudentBehaviors()).orElse(Collections.EMPTY_LIST);
                    studentBehaviorModelMapper
                            .map(ss)
                            .ifPresent(inboundStudentBehavior ->
                                    studentSchoolSession.setStudentBehaviors(studentBehaviorModelToEntityMapper
                                            .map(inboundStudentBehavior, studentBehaviors)));
                    Collection<StudentLeadershipSkill> leadershipSkills = Optional.of(studentSchoolSession)
                            .map(sy -> sy.getStudentLeadershipSkills()).orElse(Collections.EMPTY_LIST);
                    studentLeadershipSkillModelMapper
                            .map(ss)
                            .ifPresent(inboundStudentBehaviorModel ->
                                    studentSchoolSession.setStudentLeadershipSkills(studentLeadershipSkillModelToEntityMapper
                                            .map(inboundStudentBehaviorModel, leadershipSkills)));
                    Collection<StudentLeadershipTrait> leadershipTraits = Optional.of(studentSchoolSession)
                            .map(sy -> sy.getStudentLeadershipTraits()).orElse(Collections.EMPTY_LIST);
                    studentLeadershipTraitModelMapper
                            .map(ss)
                            .ifPresent(inboundStudentBehaviorModel ->
                                    studentSchoolSession.setStudentLeadershipTraits(studentLeadershipTraitModelToEntityMapper
                                            .map(inboundStudentBehaviorModel, leadershipTraits)));
//                    if (studentSchoolSession.getTeacher() == null) {
//                        studentSchoolSession.setTeacher(new StudentTeacher());
//                    }
//                    studentSchoolSession.setTeacher(studentTeacherModelToEntityMapper.map(ss.getTeacher(),
//                                    studentSchoolSession.getTeacher())
//                            .orElse(null));
//                    if (studentSchoolSession.getMentor() == null) {
//                        studentSchoolSession.setMentor(new StudentMentor());
//                    }
//                    studentSchoolSession.setMentor(studentMentorModelToEntityMapper.map(ss.getMentor(),
//                                    studentSchoolSession.getMentor())
//                            .orElse(null));
                    return studentSchoolSession;
                });
    }

}
