/*
 * Copyright 2022-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.controller;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.model.inbound.InboundSchoolSession;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentSchoolSession;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentSchoolSession;
import io.aiontechnology.mentoring.resource.SchoolSessionResource;
import io.aiontechnology.mentoring.service.SchoolService;
import io.aiontechnology.mentoring.service.SchoolSessionService;
import io.aiontechnology.mentoring.service.StudentSchoolSessionService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Controller that vents a REST interface for dealing with school sessions.
 *
 * @author Whitney Hunter
 * @since 1.8.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/schoolsessions")
@RequiredArgsConstructor
@Slf4j
public class SchoolSessionController {

    // Assemblers
    private final Assembler<SchoolSession, SchoolSessionResource> schoolSessionAssembler;

    // Mappers
    private final OneWayMapper<InboundSchoolSession, SchoolSession> schoolSessionModelToEntityMapper;
    private final OneWayCollectionMapper<StudentSchoolSession, OutboundStudentSchoolSession> studentSchoolSessionEntityToModelMapper;
    private final OneWayMapper<InboundStudentSchoolSession, StudentSchoolSession> studentSchoolSessionModelToEntityMapper;

    // Services
    private final SchoolService schoolService;
    private final SchoolSessionService schoolSessionService;
    private final StudentSchoolSessionService studentSchoolSessionService;

    /**
     * Create a new school session. The new session will be set as the current session of the school with the given
     * school id.
     *
     * @param schoolId The ID of the school for while the session is to be created.
     * @param inboundSchoolSession The data for the new school session.
     * @return The newly created school sessin.
     */
    @PostMapping
    @ResponseStatus(CREATED)
    @Transactional
    @PreAuthorize("hasAuthority('schoolsession:create')")
    public SchoolSessionResource createSchoolSession(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundSchoolSession inboundSchoolSession) {
        log.debug("Creating school session {}, for school {}", inboundSchoolSession.getLabel(), schoolId);

        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        return schoolSessionModelToEntityMapper.map(inboundSchoolSession)
                .map(newSchoolSession -> schoolSessionService.updateSchoolSession(school, newSchoolSession))
                .flatMap(schoolSessionAssembler::map)
                .orElseThrow(() -> new IllegalStateException("Unable to add new school session"));
    }

    /**
     * Delete a particular school session.
     *
     * @param schoolId The ID of the school for which the session will be deleted.
     * @param schoolSessionId The ID of the school session.
     */
    @DeleteMapping("/{schoolSessionId}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('schoolsession:delete')")
    public void deleteSchoolSession(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("schoolSessionId") UUID schoolSessionId) {
        log.debug("Deleting school session: {}", schoolSessionId);
        schoolSessionService.getSchoolSessionById(schoolSessionId)
                .ifPresent(schoolSessionService::deleteSchoolSession);
    }

    /**
     * Get all school sessions of the given school.
     *
     * @param schoolId The ID of the school for which school sessions are being requested.
     * @return A collection of school sessions for the given school.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('schoolsessions:read')")
    public CollectionModel<SchoolSessionResource> getAllSessionsForSchool(@PathVariable("schoolId") UUID schoolId) {
        log.debug("Getting all sessions for school: {}", schoolId);
        Collection<SchoolSession> sessions = schoolService.getSchoolById(schoolId)
                .map(School::getSessions)
                .orElse(Collections.EMPTY_LIST);
        Collection<SchoolSessionResource> sessionModels = sessions.stream()
                .map(schoolSessionAssembler::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        return CollectionModel.of(sessionModels);
    }

    /**
     * Get a particular school session.
     *
     * @param schoolId The ID of the school for which the school session is desired.
     * @param schoolSessionId The ID of the school session.
     * @return The school session if it is found.
     */
    @GetMapping("/{schoolSessionId}")
    @PreAuthorize("hasAuthority('schoolsession:read')")
    public SchoolSessionResource getSessionForSchool(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("schoolSessionId") UUID schoolSessionId) {
        log.debug("Getting session: {} for school: {}", schoolSessionId, schoolId);
        return schoolSessionService.getSchoolSessionById(schoolSessionId)
                .flatMap(schoolSessionAssembler::map)
                .orElseThrow(() -> new NotFoundException("School Session was not found"));
    }

}
