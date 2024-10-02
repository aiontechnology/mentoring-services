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

import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.resource.PersonResource;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.IanaLinkRelations.SELF;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class PersonAssemblerTest {

    @Test
    void testAddLinks() {
        // set up the fixture
        UUID personId = UUID.randomUUID();
        PersonAssembler assembler = new PersonAssembler();
        Person person = new Person();
        person.setId(personId);
        PersonResource resource = new PersonResource(person);

        // execute the SUT
        Set<Link> results = assembler.getLinks(resource);

        // validation
        assertThat(results.size()).isEqualTo(1);
        assertThat(results).contains(Link.of("/api/v1/people/" + personId, SELF));
    }

}
