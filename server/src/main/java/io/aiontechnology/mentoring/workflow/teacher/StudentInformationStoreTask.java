/*
 * Copyright 2023 Aion Technology LLC
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

package io.aiontechnology.mentoring.workflow.teacher;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentBehavior;
import io.aiontechnology.mentoring.entity.StudentLeadershipSkill;
import io.aiontechnology.mentoring.entity.StudentLeadershipTrait;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentInformation;
import io.aiontechnology.mentoring.service.StudentService;
import io.aiontechnology.mentoring.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentInformationStoreTask implements JavaDelegate {

    // Services
    private final StudentService studentService;

    // Mappers
    private final OneWayMapper<String, Behavior> behaviorMapper;
    private final OneWayMapper<String, LeadershipSkill> leadershipSkillMapper;
    private final OneWayMapper<String, LeadershipTrait> leadershipTraitMapper;

    // Other
    private final TaskUtilities taskUtilities;

    @Override
    public void execute(DelegateExecution execution) {
        School school = taskUtilities.getSchool(execution).orElseThrow();
        SchoolSession currentSchoolSession = school.getCurrentSession();
        Student student = taskUtilities.getStudent(execution).orElseThrow();
        SchoolPersonRole teacher = taskUtilities.getTeacher(execution).orElseThrow();
        StudentSchoolSession currentStudentSchoolSession =
                student.findCurrentSessionForStudent(currentSchoolSession).orElseThrow();

        taskUtilities.getInboundStudentInformation(execution)
                .ifPresent(studentInformation -> {
                    currentStudentSchoolSession.setStudentBehaviors(mapBehaviors(studentInformation, teacher));
                    currentStudentSchoolSession
                            .setStudentLeadershipSkills(mapLeadershipSkills(studentInformation, teacher));
                    currentStudentSchoolSession
                            .setStudentLeadershipTraits(mapLeadershipTraits(studentInformation, teacher));
                    currentStudentSchoolSession.setTeacherComment(studentInformation.getTeacherComment());
                    currentStudentSchoolSession.setPreQuestion1(studentInformation.getStudentAssessment().getQuestion1());
                    currentStudentSchoolSession.setPreQuestion2(studentInformation.getStudentAssessment().getQuestion2());
                    currentStudentSchoolSession.setPreQuestion3(studentInformation.getStudentAssessment().getQuestion3());
                    currentStudentSchoolSession.setPreQuestion4(studentInformation.getStudentAssessment().getQuestion4());
                    currentStudentSchoolSession.setPreQuestion5(studentInformation.getStudentAssessment().getQuestion5());
                    currentStudentSchoolSession.setPreQuestion6(studentInformation.getStudentAssessment().getQuestion6());
                    currentStudentSchoolSession.setPreQuestion7(studentInformation.getStudentAssessment().getQuestion7());
                    currentStudentSchoolSession.setPreQuestion8(studentInformation.getStudentAssessment().getQuestion8());
                    currentStudentSchoolSession.setPreQuestion9(studentInformation.getStudentAssessment().getQuestion9());
                    currentStudentSchoolSession.setPreQuestion10(studentInformation.getStudentAssessment().getQuestion10());
                    currentStudentSchoolSession.setPreQuestion11(studentInformation.getStudentAssessment().getQuestion11());
                    currentStudentSchoolSession.setPreQuestion12(studentInformation.getStudentAssessment().getQuestion12());
                    currentStudentSchoolSession.setPreQuestion13(studentInformation.getStudentAssessment().getQuestion13());
                    currentStudentSchoolSession.setPreQuestion14(studentInformation.getStudentAssessment().getQuestion14());
                    currentStudentSchoolSession.setPreQuestion15(studentInformation.getStudentAssessment().getQuestion15());
                    currentStudentSchoolSession.setPreQuestion16(studentInformation.getStudentAssessment().getQuestion16());
                    currentStudentSchoolSession.setPreQuestion17(studentInformation.getStudentAssessment().getQuestion17());
                    currentStudentSchoolSession.setPreQuestion18(studentInformation.getStudentAssessment().getQuestion18());
                    currentStudentSchoolSession.setPreQuestion19(studentInformation.getStudentAssessment().getQuestion19());
                    currentStudentSchoolSession.setPreQuestion20(studentInformation.getStudentAssessment().getQuestion20());
                    currentStudentSchoolSession.setPreQuestion21(studentInformation.getStudentAssessment().getQuestion21());
                    currentStudentSchoolSession.setPreQuestion22(studentInformation.getStudentAssessment().getQuestion22());
                    currentStudentSchoolSession.setPreQuestion23(studentInformation.getStudentAssessment().getQuestion23());
                    currentStudentSchoolSession.setPreQuestion24(studentInformation.getStudentAssessment().getQuestion24());
                    currentStudentSchoolSession.setPreQuestion25(studentInformation.getStudentAssessment().getQuestion25());
                    currentStudentSchoolSession.setPreQuestion26(studentInformation.getStudentAssessment().getQuestion26());
                    currentStudentSchoolSession.setPreQuestion27(studentInformation.getStudentAssessment().getQuestion27());
                    currentStudentSchoolSession.setPreQuestion28(studentInformation.getStudentAssessment().getQuestion28());
                    currentStudentSchoolSession.setPreQuestion29(studentInformation.getStudentAssessment().getQuestion29());
                    currentStudentSchoolSession.setPreQuestion30(studentInformation.getStudentAssessment().getQuestion30());
                    currentStudentSchoolSession.setPreQuestion31(studentInformation.getStudentAssessment().getQuestion31());
                    currentStudentSchoolSession.setPreQuestion32(studentInformation.getStudentAssessment().getQuestion32());
                    currentStudentSchoolSession.setPreQuestion33(studentInformation.getStudentAssessment().getQuestion33());
                    currentStudentSchoolSession.setPreQuestion34(studentInformation.getStudentAssessment().getQuestion34());
                    currentStudentSchoolSession.setPreQuestion35(studentInformation.getStudentAssessment().getQuestion35());
                    currentStudentSchoolSession.setPreBehavioralAssessment(studentInformation.getStudentAssessment().answerSum());
                    studentService.updateStudent(student);
                });
    }

    private Collection<StudentBehavior> mapBehaviors(InboundStudentInformation studentInformation,
            SchoolPersonRole teacher) {
        return studentInformation.getBehaviors().stream()
                .map(behaviorMapper::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(behavior -> {
                    StudentBehavior studentBehavior = new StudentBehavior();
                    studentBehavior.setBehavior(behavior);
                    studentBehavior.setRole(teacher);
                    return studentBehavior;
                })
                .collect(Collectors.toList());
    }

    private Collection<StudentLeadershipSkill> mapLeadershipSkills(InboundStudentInformation studentInformation,
            SchoolPersonRole teacher) {
        return studentInformation.getLeadershipSkills().stream()
                .map(leadershipSkillMapper::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(leadershipSkill -> {
                    StudentLeadershipSkill studentLeadershipSkill = new StudentLeadershipSkill();
                    studentLeadershipSkill.setLeadershipSkill(leadershipSkill);
                    studentLeadershipSkill.setRole(teacher);
                    return studentLeadershipSkill;
                })
                .collect(Collectors.toList());
    }

    private Collection<StudentLeadershipTrait> mapLeadershipTraits(InboundStudentInformation studentInformation,
            SchoolPersonRole teacher) {
        return studentInformation.getLeadershipTraits().stream()
                .map(leadershipTraitMapper::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(leadershipTrait -> {
                    StudentLeadershipTrait studentLeadershipTrait = new StudentLeadershipTrait();
                    studentLeadershipTrait.setLeadershipTrait(leadershipTrait);
                    studentLeadershipTrait.setRole(teacher);
                    return studentLeadershipTrait;
                })
                .collect(Collectors.toList());
    }

}
