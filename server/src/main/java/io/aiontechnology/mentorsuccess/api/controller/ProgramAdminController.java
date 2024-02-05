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

package io.aiontechnology.mentorsuccess.api.controller;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentorsuccess.api.assembler.Assembler;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.model.inbound.InboundProgramAdmin;
import io.aiontechnology.mentorsuccess.resource.ProgramAdminResource;
import io.aiontechnology.mentorsuccess.service.AwsService;
import io.aiontechnology.mentorsuccess.service.RoleService;
import io.aiontechnology.mentorsuccess.service.SchoolService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Session;
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

import static io.aiontechnology.mentorsuccess.model.enumeration.RoleType.PROGRAM_ADMIN;

/**
 * Controller that vends a REST interface for dealing with program admins.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/programAdmins")
@RequiredArgsConstructor
@Slf4j
public class ProgramAdminController {

    // Assemblers
    private final Assembler<SchoolPersonRole, ProgramAdminResource> programAdminAssembler;

    // Mappers
    private final OneWayMapper<Pair<InboundProgramAdmin, UUID>, SchoolPersonRole> programAdminMapper;
    private final OneWayUpdateMapper<Pair<InboundProgramAdmin, UUID>, SchoolPersonRole> programAdminUpdateMapper;

    // Services
    private final RoleService roleService;
    private final SchoolService schoolService;

    // Other
    private final AwsService awsService;
    private final EntityManager entityManager;

    /**
     * A REST endpoint for creating new program admins.
     *
     * @param schoolId The id of the school for the program admin.
     * @param inboundProgramAdmin The model that represents the desired new program admin.
     * @return A model that represents the created program admin.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('program-admin:create')")
    public ProgramAdminResource createProgramAdmin(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundProgramAdmin inboundProgramAdmin) {
        log.debug("Creating program administrator: {}", inboundProgramAdmin);
        return schoolService.getSchoolById(schoolId)
                .map(school -> Optional.ofNullable(inboundProgramAdmin)
                        .map(pa -> awsService.createAwsUser(schoolId, pa))
                        .flatMap(programAdminMapper::map)
                        .map(school::addRole)
                        .map(roleService::createRole)
                        .flatMap(programAdminAssembler::map)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to create program administrator"))
                )
                .orElseThrow(() -> new NotFoundException("School not found"));

    }

    /**
     * A REST endpoint to deactivate a specific program admin for particular school.
     *
     * @param schoolId The id of the school.
     * @param programAdminId The id of the program admin.
     */
    @DeleteMapping("/{programAdminId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('program-admin:delete')")
    public void deactivatePersonnel(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("programAdminId") UUID programAdminId) {
        log.debug("Deactivating personnel");
        roleService.findRoleById(programAdminId)
                .map(awsService::removeAwsUser)
                .ifPresent(roleService::deactivateRole);
    }

    /**
     * A REST endpoint for getting a specific program admin for a particular school.
     *
     * @param schoolId The id of the school.
     * @param programAdminId The id of the program admin.
     * @return The personnel if it could be found.
     */
    @GetMapping("/{programAdminId}")
    @PreAuthorize("hasAuthority('program-admin:read')")
    public ProgramAdminResource getPersonnel(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("programAdminId") UUID programAdminId) {
        return roleService.findRoleById(programAdminId)
                .flatMap(programAdminAssembler::map)
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for getting all program admins for a specific school.
     *
     * @param schoolId The id of the desired program admin.
     * @return A collection of {@link InboundProgramAdmin} instances.
     */
    @GetMapping
    public CollectionModel<ProgramAdminResource> getProgramAdmins(@PathVariable("schoolId") UUID schoolId) {
        log.debug("Getting all program admins for school {}", schoolId);
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("roleType").setParameter("type", PROGRAM_ADMIN.toString());
        return schoolService.getSchoolById(schoolId)
                .map(school -> school.getRoles().stream()
                        .map(programAdminAssembler::map)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                .map(programAdmins -> CollectionModel.of(programAdmins))
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for updating a program admin.
     *
     * @param schoolId The id of the school.
     * @param programAdminId The id of the program administrator to update.
     * @param inboundProgramAdmin The model that represents the updated program admin.
     * @return A model that represents the program admin that has been updated.
     */
    @PutMapping("/{programAdminId}")
    @PreAuthorize("hasAuthority('program-admin:update')")
    public ProgramAdminResource updateProgramAdmin(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("programAdminId") UUID programAdminId,
            @RequestBody @Valid InboundProgramAdmin inboundProgramAdmin) {
        awsService.updateAwsUser(inboundProgramAdmin);
        return roleService.findRoleById(programAdminId)
                .flatMap(role -> programAdminUpdateMapper.map(Pair.of(inboundProgramAdmin, role.getIdpUserId()), role))
                .map(roleService::updateRole)
                .flatMap(programAdminAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to update personnel"));
    }

}
