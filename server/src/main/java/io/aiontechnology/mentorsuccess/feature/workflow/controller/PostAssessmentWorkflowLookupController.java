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

import io.aiontechnology.mentorsuccess.api.assembler.Assembler;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.feature.workflow.model.PostAssessmentWorkflow;
import io.aiontechnology.mentorsuccess.feature.workflow.process.ProcessIdToSingleTaskFunction;
import io.aiontechnology.mentorsuccess.feature.workflow.translation.IdAndSessionToStudent;
import io.aiontechnology.mentorsuccess.feature.workflow.translation.IdToSchoolFunction;
import io.aiontechnology.mentorsuccess.feature.workflow.translation.SchoolToCurrentSessionFunction;
import io.aiontechnology.mentorsuccess.resource.PostAssessmentWorkflowResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.POST_ASSESSMENT;
import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.SCHOOL;
import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.STUDENT;

/**
 * Controller responsible for retrieving student assessments.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/students/{studentId}/workflow/assessments")
@RequiredArgsConstructor
public class PostAssessmentWorkflowLookupController {

    private final Assembler<PostAssessmentWorkflow, PostAssessmentWorkflowResource> postAssessmentWorkflowResourceAssembler;

    private final IdAndSessionToStudent getStudentFromIdAndSession;
    private final IdToSchoolFunction getSchoolFromId;
    private final SchoolToCurrentSessionFunction getCurrentSessionForSchool;
    private final ProcessIdToSingleTaskFunction getTask;

    /**
     * Find the post assessment workflow for the given school, student and assessment process id.
     *
     * @param schoolId The ID of the desired school.
     * @param studentId The ID of the desired student.
     * @param postAssessmentWorkflowId The ID of the desired post assessment workflow.
     * @return The post assessment workflow.
     */
    @GetMapping("/{postAssessmentWorkflowId}")
    public PostAssessmentWorkflowResource lookupPostAssessment(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("studentId") UUID studentId,
            @PathVariable("postAssessmentWorkflowId") UUID postAssessmentWorkflowId) {
        var schoolOption = getSchoolFromId.apply(schoolId);
        var school = schoolOption
                .orElseThrow(() -> new NotFoundException("School could not be found with ID: " + schoolId));
        var currentSessionOption = getCurrentSessionForSchool.apply(schoolOption);
        var studentOption = getStudentFromIdAndSession.apply(studentId, currentSessionOption);
        var student = studentOption
                .orElseThrow(() -> new NotFoundException("Student could not be found with ID: " + studentId));

        validateWorkflowExists(postAssessmentWorkflowId);

        var postAssessmentWorkflow = createPostAssessmentWorkflow(postAssessmentWorkflowId, student);
        var data = createDataMap(school, student, postAssessmentWorkflowId);
        return postAssessmentWorkflowResourceAssembler.mapWithData(postAssessmentWorkflow, data)
                .orElseThrow(() -> new IllegalStateException("Could not assemble the post assessment workflow"));
    }

    private Map<String, Object> createDataMap(School school, Student student, UUID postAssessmentWorkflowId) {
        return Map.of(
                SCHOOL, school,
                STUDENT, student,
                POST_ASSESSMENT, postAssessmentWorkflowId
        );
    }

    private PostAssessmentWorkflow createPostAssessmentWorkflow(UUID postAssessmentWorkflowId, Student student) {
        return PostAssessmentWorkflow.builder()
                .id(postAssessmentWorkflowId)
                .studentName(student.getFullName())
                .build();
    }

    private void validateWorkflowExists(UUID postAssessmentWorkflowId) {
        getTask.apply(postAssessmentWorkflowId)
                .orElseThrow(() -> new NotFoundException("Assessment process could not be found"));
    }

}
