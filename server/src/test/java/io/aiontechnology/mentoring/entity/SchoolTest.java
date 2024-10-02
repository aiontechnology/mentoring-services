/*
 * Copyright 2020-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.inject.Inject;
import java.util.Collections;

@DataJpaTest
public class SchoolTest {

    @Inject
    private TestEntityManager entityManager;

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
        Boolean isActive = Boolean.TRUE;
        String emailTag = "TAG";
        School school = new School(null, name, street1, street2, city, state, zip, phone, district, isPrivate, isActive,
                emailTag, Collections.EMPTY_LIST, Collections.EMPTY_LIST, null, Collections.EMPTY_LIST,
                Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        // execute the SUT
        entityManager.persist(school);

        // validation
        Assertions.assertThat(school.getId()).isNotNull();
    }

}
