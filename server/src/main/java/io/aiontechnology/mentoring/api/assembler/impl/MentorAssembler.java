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
import io.aiontechnology.mentoring.api.controller.MentorController;
import io.aiontechnology.mentoring.api.controller.SchoolController;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.resource.MentorResource;
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
public class MentorAssembler extends AssemblerSupport<SchoolPersonRole, MentorResource> {

    // Services
    private final PhoneService phoneService;

    @Override
    protected Optional<MentorResource> doMap(SchoolPersonRole mentor) {
        return Optional.ofNullable(mentor)
                .map(m -> {
                    MentorResource resource = new MentorResource(mentor);
                    Optional.ofNullable(m.getPerson())
                            .ifPresent(person -> {
                                resource.setFirstName(person.getFirstName());
                                resource.setLastName(person.getLastName());
                                resource.setEmail(person.getEmail());
                                resource.setWorkPhone(phoneService.format(person.getWorkPhone()));
                                resource.setCellPhone(phoneService.format(person.getCellPhone()));
                            });
                    resource.setId(m.getId());
                    resource.setAvailability(m.getAvailability());
                    resource.setLocation(m.getLocation());
                    resource.setMediaReleaseSigned(m.getIsMediaReleaseSigned());
                    resource.setBackgroundCheckCompleted(m.getIsBackgroundCheckCompleted());
                    return resource;
                });
    }

    @Override
    protected Set<Link> getLinks(MentorResource model) {
        SchoolPersonRole mentor = model.getContent();
        return Set.of(
                linkTo(MentorController.class, mentor.getSchool().getId()).slash(mentor.getId()).withSelfRel(),
                linkTo(SchoolController.class).slash(mentor.getSchool().getId()).withRel("school")
        );
    }

}
