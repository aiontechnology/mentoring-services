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

package io.aiontechnology.mentorsuccess.feature.workflow.process.assessment;

import io.aiontechnology.mentorsuccess.feature.workflow.translation.IdAndSessionToStudent;
import io.aiontechnology.mentorsuccess.feature.workflow.translation.IdToSchoolFunction;
import io.aiontechnology.mentorsuccess.feature.workflow.translation.SchoolToCurrentSessionFunction;
import io.aiontechnology.mentorsuccess.service.StudentService;
import io.aiontechnology.mentorsuccess.workflow.TaskUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.STUDENT_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostAssessmentPersistTask implements JavaDelegate {

    private final IdAndSessionToStudent getStudent;
    private final IdToSchoolFunction getSchool;
    private final SchoolToCurrentSessionFunction getCurrentSession;
    private final StudentService studentService;
    private final TaskUtilities taskUtilities;

    @Override
    public void execute(DelegateExecution execution) {
        var schoolId = UUID.fromString(taskUtilities.getRequiredVariable(execution, SCHOOL_ID, String.class));
        var studentId = UUID.fromString(taskUtilities.getRequiredVariable(execution, STUDENT_ID, String.class));

        var school = getSchool.apply(schoolId);
        var currentSession = getCurrentSession.apply(school);
        var student = getStudent.apply(studentId, currentSession);
        student.ifPresent(s -> {
            s.findCurrentSessionForStudent(currentSession.orElseThrow())
                    .ifPresent(session -> {
                        taskUtilities.getInboundStudentAssessment(execution)
                                .ifPresent(studentAssessment -> {
                                    session.setPostQuestion1(studentAssessment.getQuestion1());
                                    session.setPostQuestion2(studentAssessment.getQuestion2());
                                    session.setPostQuestion3(studentAssessment.getQuestion3());
                                    session.setPostQuestion4(studentAssessment.getQuestion4());
                                    session.setPostQuestion5(studentAssessment.getQuestion5());
                                    session.setPostQuestion6(studentAssessment.getQuestion6());
                                    session.setPostQuestion7(studentAssessment.getQuestion7());
                                    session.setPostQuestion8(studentAssessment.getQuestion8());
                                    session.setPostQuestion9(studentAssessment.getQuestion9());
                                    session.setPostQuestion10(studentAssessment.getQuestion10());
                                    session.setPostQuestion11(studentAssessment.getQuestion11());
                                    session.setPostQuestion12(studentAssessment.getQuestion12());
                                    session.setPostQuestion13(studentAssessment.getQuestion13());
                                    session.setPostQuestion14(studentAssessment.getQuestion14());
                                    session.setPostQuestion15(studentAssessment.getQuestion15());
                                    session.setPostQuestion16(studentAssessment.getQuestion16());
                                    session.setPostQuestion17(studentAssessment.getQuestion17());
                                    session.setPostQuestion18(studentAssessment.getQuestion18());
                                    session.setPostQuestion19(studentAssessment.getQuestion10());
                                    session.setPostQuestion20(studentAssessment.getQuestion20());
                                    session.setPostQuestion21(studentAssessment.getQuestion21());
                                    session.setPostQuestion22(studentAssessment.getQuestion22());
                                    session.setPostQuestion23(studentAssessment.getQuestion23());
                                    session.setPostQuestion24(studentAssessment.getQuestion24());
                                    session.setPostQuestion25(studentAssessment.getQuestion25());
                                    session.setPostQuestion26(studentAssessment.getQuestion26());
                                    session.setPostQuestion27(studentAssessment.getQuestion27());
                                    session.setPostQuestion28(studentAssessment.getQuestion28());
                                    session.setPostQuestion29(studentAssessment.getQuestion29());
                                    session.setPostQuestion30(studentAssessment.getQuestion30());
                                    session.setPostQuestion31(studentAssessment.getQuestion31());
                                    session.setPostQuestion32(studentAssessment.getQuestion32());
                                    session.setPostQuestion33(studentAssessment.getQuestion33());
                                    session.setPostQuestion34(studentAssessment.getQuestion34());
                                    session.setPostQuestion35(studentAssessment.getQuestion35());
                                    session.setPostBehavioralAssessment(studentAssessment.answerSum());
                                    studentService.updateStudent(s);
                                });
                    });

        });
    }

}