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

package io.aiontechnology.mentorsuccess.repository;

import io.aiontechnology.mentorsuccess.entity.School;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.inject.Inject;
import java.util.Optional;

@DataJpaTest
public class SchoolRepositoryTest {

    @Inject
    private TestEntityManager entityManager;

    @Inject
    private SchoolRepository schoolRepository;

    @Test
    void testDelete() {
        // setup the fixture
        String name = "NAME";
        String street1 = "STREET1";
        String street2 = "STREET2";
        String city = "CITY";
        String state = "ST";
        String zip = "123456789";
        String phone = "1234567890";
        String district = "DISTRICT";
        Boolean isPrivate = Boolean.TRUE;
        School school = new School(null, name, street1, street2, city, state, zip, phone, district, isPrivate);
        entityManager.persist(school);

        // execute the SUT
        schoolRepository.delete(school);

        // validation
        School result = entityManager.find(School.class, school.getId());
        Assertions.assertThat(result).isNull();
    }

    @Test
    void testFindById() {
        // setup the fixture
        String name = "NAME";
        String street1 = "STREET1";
        String street2 = "STREET2";
        String city = "CITY";
        String state = "ST";
        String zip = "123456789";
        String phone = "1234567890";
        String district = "DISTRICT";
        Boolean isPrivate = Boolean.TRUE;
        School school = new School(null, name, street1, street2, city, state, zip, phone, district, isPrivate);
        entityManager.persist(school);

        // exercise the SUT
        Optional<School> result = schoolRepository.findById(school.getId());

        // validation
        Assertions.assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void testSave() {
        // setup the fixture
        String name = "NAME";
        String street1 = "STREET1";
        String street2 = "STREET2";
        String city = "CITY";
        String state = "ST";
        String zip = "123456789";
        String phone = "1234567890";
        String district = "DISTRICT";
        Boolean isPrivate = Boolean.TRUE;
        School school = new School(null, name, street1, street2, city, state, zip, phone, district, isPrivate);

        // execute the SUT
        schoolRepository.save(school);

        // validation
        School result = entityManager.find(School.class, school.getId());
        Assertions.assertThat(result).isEqualTo(school);
    }
}
