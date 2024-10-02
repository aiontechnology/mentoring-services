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
import io.aiontechnology.mentoring.api.controller.PersonnelController;
import io.aiontechnology.mentoring.api.controller.SchoolController;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.resource.PersonnelResource;
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
public class PersonnelAssembler extends AssemblerSupport<SchoolPersonRole, PersonnelResource> {

    private final PhoneService phoneService;

    @Override
    protected Optional<PersonnelResource> doMap(SchoolPersonRole personnel) {
        return Optional.ofNullable(personnel)
                .map(p -> {
                    PersonnelResource resource = new PersonnelResource(personnel);
                    Optional.ofNullable(p.getPerson())
                            .ifPresent(person -> {
                                resource.setFirstName(person.getFirstName());
                                resource.setLastName(person.getLastName());
                                resource.setEmail(person.getEmail());
                                resource.setWorkPhone(phoneService.format(person.getWorkPhone()));
                                resource.setCellPhone(phoneService.format(person.getCellPhone()));
                            });
                    resource.setType(p.getType());
                    return resource;
                });
    }

    @Override
    protected Set<Link> getLinks(PersonnelResource model) {
        SchoolPersonRole personnel = model.getContent();
        return Set.of(
                linkTo(PersonnelController.class, personnel.getSchool().getId()).slash(personnel.getId()).withSelfRel(),
                linkTo(SchoolController.class).slash(personnel.getSchool().getId()).withRel("school")
        );
    }

}
