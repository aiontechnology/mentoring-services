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

import io.aiontechnology.mentoring.api.assembler.ReflectionAssemblerSupport;
import io.aiontechnology.mentoring.api.controller.SchoolSessionController;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.resource.SchoolSessionResource;
import org.springframework.hateoas.Link;

import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class SchoolSessionAssembler extends ReflectionAssemblerSupport<SchoolSession, SchoolSessionResource> {

    public SchoolSessionAssembler() {
        super(SchoolSessionResource.class);
    }

    @Override
    public Optional<SchoolSessionResource> map(SchoolSession schoolSession) {
        boolean isCurrent = Optional.ofNullable(schoolSession)
                .map(SchoolSession::getSchool)
                .map(School::getCurrentSession)
                .map(currentSession -> currentSession.equals(schoolSession))
                .orElse(false);

        return Optional.ofNullable(schoolSession)
                .flatMap(super::map)
                .map(schoolSessionResource -> {
                    schoolSessionResource.setIsCurrent(isCurrent);
                    return schoolSessionResource;
                });
    }

    @Override
    protected Set<Link> getLinks(SchoolSessionResource model) {
        SchoolSession session = model.getContent();
        return Set.of(
                linkTo(SchoolSessionController.class, session.getSchool().getId()).slash(session.getId()).withSelfRel()
        );
    }

}
