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

package io.aiontechnology.mentorsuccess.api.assembler.impl;

import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.resource.MentorResource;
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
public class MentorAssemblerTest {

    @Test
    void testAddLinks() {
        // set up the fixture
        UUID schoolId = UUID.randomUUID();
        School school = new School();
        school.setId(schoolId);

        UUID mentorId = UUID.randomUUID();
        MentorAssembler assembler = new MentorAssembler(null);
        SchoolPersonRole mentor = new SchoolPersonRole();
        mentor.setId(mentorId);
        mentor.setSchool(school);
        MentorResource resource = new MentorResource(mentor);

        // execute the SUT
        Set<Link> results = assembler.getLinks(resource);

        // validation
        assertThat(results.size()).isEqualTo(2);
        assertThat(results).contains(Link.of("/api/v1/schools/" + schoolId.toString() + "/mentors/" + mentorId, SELF));
        assertThat(results).contains(Link.of("/api/v1/schools/" + schoolId.toString(), "School"));
    }

}
