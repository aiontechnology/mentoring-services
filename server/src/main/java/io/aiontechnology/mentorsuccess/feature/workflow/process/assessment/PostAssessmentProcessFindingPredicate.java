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

import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.entity.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.BiPredicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostAssessmentProcessFindingPredicate implements BiPredicate<Optional<Student>, Optional<SchoolSession>> {

    // Services
    private final PostAssessmentProcessFindingFunction findFlowableProcess;

    @Override
    public boolean test(Optional<Student> student, Optional<SchoolSession> session) {
        var isProcessRunning = findFlowableProcess.apply(student, session).isPresent();
        log.debug("Looking up process for student:{} <session: {}>: {}",
                student.map(Student::getFullName).orElse("<unknown>"),
                session.map(SchoolSession::getLabel).orElse("<unknown>"),
                isProcessRunning ? "found" : "not found");
        return isProcessRunning;
    }

}
