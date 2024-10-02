/*
 * Copyright 2023 Aion Technology LLC
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
import io.aiontechnology.mentoring.api.controller.InterestController;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.resource.InterestResource;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
public class InterestAssembler extends AssemblerSupport<Interest, InterestResource> {

    @Override
    protected Optional<InterestResource> doMap(Interest interest) {
        return Optional.ofNullable(interest)
                .map(i -> {
                    InterestResource resource = new InterestResource(interest);
                    resource.setId(i.getId());
                    resource.setName(i.getName());
                    return resource;
                });
    }

    @Override
    protected Set<Link> getLinks(InterestResource model) {
        Interest interest = model.getContent();
        return Set.of(
                linkTo(InterestController.class).slash(interest.getId()).withSelfRel()
        );
    }

}
