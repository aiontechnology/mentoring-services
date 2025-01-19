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
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.model.inbound.InboundPersonnel;
import io.aiontechnology.mentoring.resource.PersonnelResource;
import io.aiontechnology.mentoring.service.RoleService;
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

import static io.aiontechnology.mentoring.model.enumeration.RoleType.COUNSELOR;
import static io.aiontechnology.mentoring.model.enumeration.RoleType.PRINCIPAL;
import static io.aiontechnology.mentoring.model.enumeration.RoleType.SOCIAL_WORKER;
import static io.aiontechnology.mentoring.model.enumeration.RoleType.STAFF;

/**
 * Controller that vends a REST interface for dealing with personnel.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/personnel")
@RequiredArgsConstructor
@Slf4j
public class PersonnelController {

    // Assemblers
    private final Assembler<SchoolPersonRole, PersonnelResource> personnelAssembler;

    // Mappers
    private final OneWayMapper<InboundPersonnel, SchoolPersonRole> personnelMapper;
    private final OneWayUpdateMapper<InboundPersonnel, SchoolPersonRole> personnelUpdateMapper;

    // Services
    private final RoleService roleService;
    private final SchoolService schoolService;

    /**
     * A REST endpoint for creating a personnel for a particular school.
     *
     * @param schoolId The id of the school.
     * @param inboundPersonnel The model that represents the desired new personnel.
     * @return A model that represents the created personnel.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('personnel:create')")
    public PersonnelResource createPersonnel(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundPersonnel inboundPersonnel) {
        log.debug("Creating personnel: {}", inboundPersonnel);
        return schoolService.getSchoolById(schoolId)
                .map(school -> Optional.ofNullable(inboundPersonnel)
                        .flatMap(personnelMapper::map)
                        .map(school::addRole)
                        .map(roleService::createRole)
                        .flatMap(personnelAssembler::map)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to create personnel")))
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint to deactivate a specific personnel for particular school.
     *
     * @param schoolId The id of the school.
     * @param personnelId The id of the personnel.
     */
    @DeleteMapping("/{personnelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('personnel:delete')")
    public void deactivatePersonnel(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("personnelId") UUID personnelId) {
        log.debug("Deactivating personnel");
        roleService.findRoleById(personnelId)
                .ifPresent(roleService::deactivateRole);
    }

    /**
     * A REST endpoint for getting all personnel for a particular school.
     *
     * @param schoolId The id of the school.
     * @return A collection of all personnel for the school.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('personnels:read')")
    public CollectionModel<PersonnelResource> getAllPersonnel(@PathVariable("schoolId") UUID schoolId) {
        log.debug("Getting all personnel for school {}", schoolId);
        return schoolService.getSchoolById(schoolId)
                .map(school -> school.getRoles().stream()
                        .filter(role -> role.getType().equals(SOCIAL_WORKER) ||
                                role.getType().equals(PRINCIPAL) ||
                                role.getType().equals(COUNSELOR) ||
                                role.getType().equals(STAFF))
                        .map(personnelAssembler::map)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                .map(teachers -> CollectionModel.of(teachers))
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for getting a specific personnel for a particular school.
     *
     * @param schoolId The id of the school.
     * @param personnelId The id of the personnel.
     * @return The personnel if it could be found.
     */
    @GetMapping("/{personnelId}")
    @PreAuthorize("hasAuthority('personnel:read')")
    public PersonnelResource getPersonnel(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("personnelId") UUID personnelId) {
        return roleService.findRoleById(personnelId)
                .flatMap(personnelAssembler::map)
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for updating a personnel.
     *
     * @param schoolId The id of the school.
     * @param personnelId The id of the personnel to update
     * @param inboundPersonnel The model that represents the updated personnel.
     * @return A model that represents the personnel that has been updated.
     */
    @PutMapping("/{personnelId}")
    @PreAuthorize("hasAuthority('personnel:update')")
    public PersonnelResource updateSchool(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("personnelId") UUID personnelId,
            @RequestBody @Valid InboundPersonnel inboundPersonnel) {
        return roleService.findRoleById(personnelId)
                .flatMap(role -> personnelUpdateMapper.map(inboundPersonnel, role))
                .map(roleService::updateRole)
                .flatMap(personnelAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to update personnel"));
    }

}
