/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
import io.aiontechnology.mentorsuccess.api.assembler.LinkProvider;
import io.aiontechnology.mentorsuccess.api.assembler.MentorModelAssembler;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.model.inbound.InboundMentor;
import io.aiontechnology.mentorsuccess.model.outbound.OutboundMentor;
import io.aiontechnology.mentorsuccess.service.RoleService;
import io.aiontechnology.mentorsuccess.service.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.aiontechnology.mentorsuccess.model.enumeration.RoleType.MENTOR;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Controller that vends a REST interface for dealing with mentors.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/mentors")
@RequiredArgsConstructor
@Slf4j
public class MentorController {

    /** The JPA entity manager */
    private final EntityManager entityManager;

    /** A mapper for converting {@link InboundMentor} instances to {@link SchoolPersonRole Roles}. */
    private final OneWayMapper<InboundMentor, SchoolPersonRole> mentorMapper;

    /** An update mapper for converting {@link InboundMentor} instances to {@link SchoolPersonRole Roles}. */
    private final OneWayUpdateMapper<InboundMentor, SchoolPersonRole> mentorUpdateMapper;

    /** Assembler for creating {@link OutboundMentor} instances */
    private final MentorModelAssembler mentorModelAssembler;

    /** Service with business logic for teachers */
    private final RoleService roleService;

    /** Service with business logic for schools */
    private final SchoolService schoolService;

    /** {@link LinkProvider} implementation for mentors. */
    private final LinkProvider<OutboundMentor, SchoolPersonRole> linkProvider = (mentorModel, role) ->
            Arrays.asList(
                    linkTo(MentorController.class, role.getSchool().getId()).slash(role.getId()).withSelfRel(),
                    linkTo(SchoolController.class).slash(role.getSchool().getId()).withRel("school")
            );

    /**
     * A REST endpoint for creating a mentor for a particular school.
     *
     * @param schoolId The id of the school.
     * @param mentorModel The model that represents the desired new mentor.
     * @return A model that represents the created mentor.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutboundMentor createMentor(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundMentor mentorModel) {
        log.debug("Creating mentor: {}", mentorModel);
        return schoolService.getSchoolById(schoolId)
                .map(school -> Optional.ofNullable(mentorModel)
                        .flatMap(mentorMapper::map)
                        .map(school::addRole)
                        .map(roleService::createRole)
                        .map(role -> mentorModelAssembler.toModel(role, linkProvider))
                        .orElseThrow(() -> new IllegalArgumentException("Unable to create mentor")))
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    /**
     * A REST endpoint for retrieving all mentors.
     *
     * @param schoolId The id of the school.
     * @return A collection of {@link OutboundMentor} instances for the requested school.
     */
    @GetMapping
    public CollectionModel<OutboundMentor> getMentors(@PathVariable("schoolId") UUID schoolId) {
        log.debug("Getting all mentors for school {}", schoolId);
        var session = entityManager.unwrap(Session.class);
        session.enableFilter("roleType").setParameter("type", MENTOR.toString());
        return schoolService.getSchoolById(schoolId)
                .map(school -> school.getRoles().stream()
                        .map(role -> mentorModelAssembler.toModel(role, linkProvider))
                        .collect(Collectors.toList()))
                .map(CollectionModel::of)
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for getting a specific mentor for a particular school.
     *
     * @param schoolId The id of the school.
     * @param mentorId The id of the teacher.
     * @return The personnel if it could be found.
     */
    @GetMapping("/{mentorId}")
    public OutboundMentor getTeacher(@PathVariable("schoolId") UUID schoolId, @PathVariable("mentorId") UUID mentorId) {
        return roleService.findRoleById(mentorId)
                .map(role -> mentorModelAssembler.toModel(role, linkProvider))
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for updating a mentor.
     *
     * @param schoolId The id of the school.
     * @param mentorId The model that represents the updated mentor.
     * @param mentorModel The model of the updated mentor.
     * @return A model that represents the teacher that has been updated.
     */
    @PutMapping("/{mentorId}")
    public OutboundMentor updateMentor(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("mentorId") UUID mentorId,
            @RequestBody @Valid InboundMentor mentorModel) {
        return roleService.findRoleById(mentorId)
                .flatMap(role -> mentorUpdateMapper.map(mentorModel, role))
                .map(roleService::updateRole)
                .map(role -> mentorModelAssembler.toModel(role, linkProvider))
                .orElseThrow(() -> new IllegalArgumentException("Unable to update mentor"));
    }

    /**
     * A REST endpoint for deactivating a teacher.
     *
     * @param studentId The school from which the teacher should be deactivated.
     * @param mentorId The id of the mentor to remove.
     */
    @DeleteMapping("/{mentorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateMentor(@PathVariable("schoolId") UUID studentId, @PathVariable("mentorId") UUID mentorId) {
        log.debug("Deactivating mentor");
        roleService.findRoleById(mentorId)
                .ifPresent(roleService::deactivateRole);
    }

}
