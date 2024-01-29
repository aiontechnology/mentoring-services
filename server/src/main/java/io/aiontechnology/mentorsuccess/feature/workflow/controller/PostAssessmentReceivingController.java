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

package io.aiontechnology.mentorsuccess.feature.workflow.controller;

import io.aiontechnology.mentorsuccess.feature.workflow.process.TaskCompletionConsumer;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentAssessment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.SHOULD_CANCEL;
import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.STUDENT_ASSESSMENT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/schools/{schoolId}/students/{studentId}/assessments")
@RequiredArgsConstructor
@Slf4j
public class PostAssessmentReceivingController {

    private final TaskCompletionConsumer taskCompletionConsumer;

    @PostMapping("/{assessmentProcessId}")
    @ResponseStatus(CREATED)
    public void receivePostAssessment(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("studentId") UUID studentId, @PathVariable("assessmentProcessId") UUID assessmentProcessId,
            @RequestBody @Valid InboundStudentAssessment assessment) {
        log.debug("Received post assessment for student {}: {}", studentId, assessment);
        taskCompletionConsumer.accept(assessmentProcessId,
                Map.of(SHOULD_CANCEL, false, STUDENT_ASSESSMENT, assessment));
    }

}
