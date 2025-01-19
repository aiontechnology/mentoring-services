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

package io.aiontechnology.mentoring.feature.workflow.process.assessment;

import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.feature.workflow.translation.SessionToStudentsFunction;
import io.aiontechnology.mentoring.feature.workflow.translation.StudentToCurrentTeacherFunction;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static io.aiontechnology.mentoring.feature.workflow.WorkflowKeys.PROCESS_TIMEOUT_VALUE;
import static io.aiontechnology.mentoring.feature.workflow.WorkflowKeys.REQUEST_POST_ASSESSMENT_PROCESS_KEY;

/**
 * Executes the post-assessment initiation process for a school session and base URI.
 */
@Component
@RequiredArgsConstructor
public class PostAssessmentInitiatingConsumer implements BiConsumer<Optional<SchoolSession>, URI> {

    // Services
    private final PostAssessmentProcessFindingPredicate isProcessRunning;
    private final PostAssessmentVariableCreator createFlowableVariables;
    private final RuntimeService runtimeService;
    private final SessionToStudentsFunction getStudentsForSession;
    private final StudentToCurrentTeacherFunction getCurrentTeacherForStudentAndSession;

    /**
     * Executes the post-assessment  process for a school session and base URI if the process is not currently running.
     *
     * @param session The current school session.
     * @param baseUri The base URI for links.
     */
    @Override
    public void accept(Optional<SchoolSession> session, URI baseUri) {
        Predicate<Optional<Student>> noProcessRunning = student -> !isProcessRunning.test(student, session);
        Predicate<Optional<Student>> noPostAssessment = student -> !testPostAssessment(student, session);
        getStudentsForSession(session).stream()
                .map(Optional::of)
                .filter(noProcessRunning)
                .filter(noPostAssessment)
                .forEach(student -> startProcess(student, session, baseUri));
    }

    private Collection<Student> getStudentsForSession(Optional<SchoolSession> session) {
        return session
                .map(getStudentsForSession)
                .orElse(Collections.emptyList());
    }

    private void startProcess(Optional<Student> student, Optional<SchoolSession> session, URI baseUri) {
        var teacher = getCurrentTeacherForStudentAndSession.apply(Pair.of(student, session));
        var variables = createFlowableVariables.create(student, session, teacher, baseUri, PROCESS_TIMEOUT_VALUE);
        runtimeService.startProcessInstanceByKey(REQUEST_POST_ASSESSMENT_PROCESS_KEY, variables);
    }

    private boolean testPostAssessment(Optional<Student> student, Optional<SchoolSession> session) {
        var currentSession = session.orElseThrow();
        Integer postAssessment = student
                .map(s ->
                        s.findCurrentSessionForStudent(currentSession)
                                .map(StudentSchoolSession::getPostBehavioralAssessment)
                                .orElse(null))
                .orElse(null);
        return postAssessment != null && postAssessment > 0;
    }

}
