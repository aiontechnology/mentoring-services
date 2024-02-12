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

package io.aiontechnology.mentorsuccess.api.assembler.impl;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.api.assembler.Assembler;
import io.aiontechnology.mentorsuccess.api.assembler.AssemblerSupport;
import io.aiontechnology.mentorsuccess.api.controller.SchoolController;
import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.model.outbound.OutboundAddress;
import io.aiontechnology.mentorsuccess.resource.SchoolResource;
import io.aiontechnology.mentorsuccess.resource.SchoolSessionResource;
import io.aiontechnology.mentorsuccess.util.PhoneService;
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
public class SchoolAssembler extends AssemblerSupport<School, SchoolResource> {

    // Assemblers
    private final Assembler<SchoolSession, SchoolSessionResource> schoolSessionAssembler;

    // Mappers
    private final OneWayMapper<School, OutboundAddress> addressMapper;

    // Services
    private final PhoneService phoneService;

    @Override
    protected Optional<SchoolResource> doMap(School school) {
        return Optional.ofNullable(school)
                .map(s -> {
                    SchoolResource resource = new SchoolResource(school);
                    resource.setId(school.getId());
                    resource.setName(school.getName());
                    resource.setAddress(addressMapper.map(school).orElse(null));
                    resource.setPhone(phoneService.format(school.getPhone()));
                    resource.setDistrict(school.getDistrict());
                    resource.setIsPrivate(school.getIsPrivate());
                    resource.setEmailTag(school.getEmailTag());
                    resource.setCurrentSession(schoolSessionAssembler.map(school.getCurrentSession()).orElse(null));
                    return resource;
                });
    }

    @Override
    protected Set<Link> getLinks(SchoolResource model) {
        School school = model.getContent();
        return Set.of(
                linkTo(SchoolController.class).slash(school.getId()).withSelfRel(),
                linkTo(SchoolController.class).slash(school.getId()).slash("teachers").withRel("teachers"),
                linkTo(SchoolController.class).slash(school.getId()).slash("programAdmins").withRel("programAdmins")
        );
    }

}
