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

package io.aiontechnology.mentoring.api.mapping.tomodel.misc;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.api.controller.PersonController;
import io.aiontechnology.mentoring.entity.Person;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Mapper that converts a {@link Person} to a URI.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
public class PersonEntityToUriMapper implements OneWayMapper<Person, URI> {

    /**
     * Map the given {@link Person} to a URI.
     *
     * @param person The {@link Person} to map.
     * @return The resulting URI.
     */
    @Override
    public Optional<URI> map(Person person) {
        return Optional.ofNullable(person)
                .map(p -> methodOn(PersonController.class).getPerson(p.getId()))
                .map(WebMvcLinkBuilder::linkTo)
                .map(WebMvcLinkBuilder::toUri);
    }

}
