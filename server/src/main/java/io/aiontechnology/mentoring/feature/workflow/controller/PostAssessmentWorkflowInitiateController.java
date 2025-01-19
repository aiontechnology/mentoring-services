/*
 * Copyright 2023-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.feature.workflow.controller;

import io.aiontechnology.mentoring.feature.workflow.process.assessment.PostAssessmentInitiatingConsumer;
import io.aiontechnology.mentoring.feature.workflow.translation.IdToSchoolFunction;
import io.aiontechnology.mentoring.feature.workflow.translation.SchoolToCurrentSessionFunction;
import io.aiontechnology.mentoring.model.inbound.InboundPostAssessment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Controller for interacting with student assessments.
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/workflow/assessments")
@RequiredArgsConstructor
@Slf4j
public class PostAssessmentWorkflowInitiateController {

    // Services
    private final IdToSchoolFunction getSchoolFromId;
    private final PostAssessmentInitiatingConsumer initiatePostAssessments;
    private final SchoolToCurrentSessionFunction getCurrentSessionFromSchool;

    /**
     * Request post assessments for all students at the given school in the current session.
     *
     * @param schoolId The ID of the desired school.
     */
    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('school:update')")
    public void initiatePostAssessmentsForSchool(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundPostAssessment inboundPostAssessment) {
        Optional.ofNullable(schoolId)
                .map(getSchoolFromId)
                .map(getCurrentSessionFromSchool)
                .ifPresent(currentSession -> initiatePostAssessments.accept(currentSession,
                        URI.create(inboundPostAssessment.getPostAssessmentUri())));
    }

}
