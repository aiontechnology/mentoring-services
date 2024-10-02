/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.assembler.impl;

import io.aiontechnology.mentoring.api.assembler.AssemblerSupport;
import io.aiontechnology.mentoring.api.controller.ProgramAdminController;
import io.aiontechnology.mentoring.api.controller.SchoolController;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.resource.ProgramAdminResource;
import io.aiontechnology.mentoring.util.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@RequiredArgsConstructor
public class ProgramAdminAssembler extends AssemblerSupport<SchoolPersonRole, ProgramAdminResource> {

    private final PhoneService phoneService;

    @Override
    protected Optional<ProgramAdminResource> doMap(SchoolPersonRole programAdmin) {
        return Optional.ofNullable(programAdmin)
                .map(p -> {
                    ProgramAdminResource resource = new ProgramAdminResource(programAdmin);
                    Optional.ofNullable(p.getPerson())
                            .ifPresent(person -> {
                                resource.setFirstName(person.getFirstName());
                                resource.setLastName(person.getLastName());
                                resource.setEmail(person.getEmail());
                                resource.setWorkPhone(phoneService.format(person.getWorkPhone()));
                                resource.setCellPhone(phoneService.format(person.getCellPhone()));
                            });
                    return resource;
                });
    }

    @Override
    protected Set<Link> getLinks(ProgramAdminResource model) {
        SchoolPersonRole programAdmin = model.getContent();
        return Set.of(
                linkTo(ProgramAdminController.class, programAdmin.getSchool().getId()).slash(programAdmin.getId()).withSelfRel(),
                linkTo(SchoolController.class).slash(programAdmin.getSchool().getId()).withRel("school")
        );
    }

}
