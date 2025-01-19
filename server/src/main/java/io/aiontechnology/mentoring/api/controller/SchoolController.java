/*
 * Copyright 2020-2024 Aion Technology LLC
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

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.inbound.InboundSchool;
import io.aiontechnology.mentoring.resource.SchoolResource;
import io.aiontechnology.mentoring.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller that vends a REST interface for dealing with schools.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
@Slf4j
public class SchoolController {

    // Assemblers
    private final Assembler<School, SchoolResource> schoolAssembler;

    // Mappers
    private final OneWayMapper<InboundSchool, School> schoolMapper;
    private final OneWayUpdateMapper<InboundSchool, School> schoolUpdateMapper;

    // Services
    private final SchoolService schoolService;

    /**
     * A REST endpoint for creating new schools.
     *
     * @param inboundSchool The model that represents the desired new school.
     * @return A model that represents the created school.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('school:create')")
    public SchoolResource createSchool(@RequestBody @Valid InboundSchool inboundSchool) {
        log.debug("Creating school: {}", inboundSchool);
        return Optional.ofNullable(inboundSchool)
                .flatMap(schoolMapper::map)
                .map(schoolService::createSchool)
                .map(s -> schoolService.setInitialSession(s, inboundSchool.getInitialSessionLabel()))
                .flatMap(schoolAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create school"));
    }

    /**
     * A REST endpoint for deleting a school.
     *
     * @param schoolId The id of the school to remove.
     */
    @DeleteMapping("/{schoolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('school:delete')")
    public void deactivateSchool(@PathVariable("schoolId") UUID schoolId) {
        log.debug("Deactivating school: {}", schoolId);
        schoolService.getSchoolById(schoolId)
                .map(schoolService::removeAllProgramAdministrators)
                .ifPresent(schoolService::deactivateSchool);
    }

    /**
     * A REST endpoint for retrieving all schools.
     *
     * @return A collection of {@link InboundSchool} instances for all schools.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('schools:read')")
    public CollectionModel<SchoolResource> getAllSchools() {
        log.debug("Getting all schools");
        var schools = StreamSupport.stream(schoolService.getAllSchools().spliterator(), false)
                .map(schoolAssembler::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return CollectionModel.of(schools);
    }

    /**
     * A REST endpoint for retrieving a particular school.
     *
     * @param schoolId The id of the desired school
     * @return The school if it is found.
     */
    @GetMapping("/{schoolId}")
    @PreAuthorize("hasAuthority('school:read')")
    public SchoolResource getSchool(@PathVariable("schoolId") UUID schoolId) {
        log.debug("Getting school with id {}", schoolId);
        Optional<School> school = schoolService.getSchoolById(schoolId);
        return school
                .flatMap(schoolAssembler::map)
                .orElseThrow(() -> new NotFoundException("School was not found"));
    }

    /**
     * A REST endpoint for updating a school.
     *
     * @param schoolId The id of the school to update.
     * @param inboundSchool The model that represents the updated school.
     * @return A model that represents the school that has been updated.
     */
    @PutMapping("/{schoolId}")
    @PreAuthorize("hasAuthority('school:update')")
    public SchoolResource updateSchool(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundSchool inboundSchool) {
        log.debug("Updating school {} with {}", schoolId, inboundSchool);
        return schoolService.getSchoolById(schoolId)
                .flatMap(school -> schoolUpdateMapper.map(inboundSchool, school))
                .map(schoolService::updateSchool)
                .flatMap(schoolAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to update school"));
    }

}
