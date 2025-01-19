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

import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentoring.model.enumeration.RoleType.PRINCIPAL;
import static io.aiontechnology.mentoring.model.enumeration.RoleType.TEACHER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link TeacherEntityToUriMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class TeacherEntityToUriMapperTest {

    @Test
    void testMapWithSingleSchool() throws Exception {
        // setup the fixture
        UUID schoolId = UUID.randomUUID();
        School school = new School();
        school.setId(schoolId);

        UUID roleId = UUID.randomUUID();
        SchoolPersonRole schoolPersonRole = createRoleAndPerson(roleId);
        schoolPersonRole.setType(TEACHER);

        TeacherEntityToUriMapper teacherEntityToUriMapper = new TeacherEntityToUriMapper();

        // execute the SUT
        Optional<URI> result = teacherEntityToUriMapper.map(school, schoolPersonRole);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get().toString()).isEqualTo("/api/v1/schools/" + schoolId + "/teachers/" + roleId);
    }

    @Test
    void testMapWithMultipleSchools() throws Exception {
        // setup the fixture
        UUID schoolId1 = UUID.randomUUID();
        School school1 = new School();
        school1.setId(schoolId1);
        UUID schoolId2 = UUID.randomUUID();
        School school2 = new School();
        school2.setId(schoolId2);

        UUID roleId1 = UUID.randomUUID();
        SchoolPersonRole schoolPersonRole1 = createRoleAndPerson(roleId1);
        schoolPersonRole1.setType(TEACHER);
        UUID roleId2 = UUID.randomUUID();
        SchoolPersonRole schoolPersonRole2 = createRoleAndPerson(roleId2);
        schoolPersonRole2.setType(TEACHER);

        TeacherEntityToUriMapper teacherEntityToUriMapper = new TeacherEntityToUriMapper();

        // execute the SUT
        Optional<URI> result1 = teacherEntityToUriMapper.map(school1, schoolPersonRole1);
        Optional<URI> result2 = teacherEntityToUriMapper.map(school2, schoolPersonRole2);

        // validation
        assertThat(result1).isNotEmpty();
        assertThat(result1.get().toString()).isEqualTo("/api/v1/schools/" + schoolId1 + "/teachers/" + roleId1);
        assertThat(result2).isNotEmpty();
        assertThat(result2.get().toString()).isEqualTo("/api/v1/schools/" + schoolId2 + "/teachers/" + roleId2);
    }

    @Test
    void testMapWithWrongRoleType() throws Exception {
        // setup the fixture
        UUID schoolId = UUID.randomUUID();
        School school = new School();
        school.setId(schoolId);

        UUID roleId = UUID.randomUUID();
        SchoolPersonRole schoolPersonRole = createRoleAndPerson(roleId);

        TeacherEntityToUriMapper teacherEntityToUriMapper = new TeacherEntityToUriMapper();

        // execute the SUT
        Optional<URI> result = teacherEntityToUriMapper.map(school, schoolPersonRole);

        // validation
        assertThat(result).isEmpty();
    }

    private SchoolPersonRole createRoleAndPerson(UUID roleId) {
        UUID personId = UUID.randomUUID();
        Person person = new Person();
        person.setId(personId);

        SchoolPersonRole role = new SchoolPersonRole();
        role.setId(roleId);
        role.setType(PRINCIPAL);

        role.setPerson(person);
//        person.setRoles(Arrays.asList(role));
        return role;
    }

}
