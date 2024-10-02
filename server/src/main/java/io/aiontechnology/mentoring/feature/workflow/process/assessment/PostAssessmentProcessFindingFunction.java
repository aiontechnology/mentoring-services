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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.BiFunction;

import static io.aiontechnology.mentoring.feature.workflow.WorkflowKeys.REQUEST_POST_ASSESSMENT_PROCESS_KEY;
import static io.aiontechnology.mentoring.feature.workflow.WorkflowKeys.SCHOOL_SESSION_ID;
import static io.aiontechnology.mentoring.feature.workflow.WorkflowKeys.STUDENT_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostAssessmentProcessFindingFunction implements
        BiFunction<Optional<Student>, Optional<SchoolSession>, Optional<ProcessInstance>> {

    // Services
    private final RuntimeService runtimeService;

    @Override
    public Optional<ProcessInstance> apply(Optional<Student> student, Optional<SchoolSession> session) {
        var query = createProcessQuery(student, session);
        return Optional.ofNullable(query.singleResult());
    }

    private ProcessInstanceQuery createProcessQuery(Optional<Student> student, Optional<SchoolSession> session) {
        var query = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(REQUEST_POST_ASSESSMENT_PROCESS_KEY)
                .active();
        student.ifPresent(s -> query.variableValueEquals(STUDENT_ID, s.getId().toString()));
        session.ifPresent(s -> query.variableValueEquals(SCHOOL_SESSION_ID, s.getId().toString()));
        return query;
    }

}
