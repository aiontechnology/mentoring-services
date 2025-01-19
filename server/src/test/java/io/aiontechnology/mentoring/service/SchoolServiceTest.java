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

package io.aiontechnology.mentoring.service;

import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.entity.Game;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.repository.SchoolRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchoolServiceTest {

    private static final String NAME = "NAME";
    private static final String STREET1 = "STREET1";
    private static final String STREET2 = "STREET2";
    private static final String CITY = "CITY";
    private static final String STATE = "ST";
    private static final String ZIP = "123456789";
    private static final String PHONE = "1234567890";
    private static final String DISTRICT = "DISTRICT";
    private static final Boolean IS_PRIVATE = Boolean.TRUE;
    private static final Boolean IS_ACTIVE = Boolean.TRUE;
    private static final String EMAIL_TAG = "TAG";
    private static final Collection<SchoolPersonRole> ROLES = Collections.EMPTY_LIST;
    private static final Collection<Student> STUDENTS = Collections.EMPTY_LIST;
    private static final Collection<Book> BOOKS = Collections.EMPTY_LIST;
    private static final Collection<Game> GAMES = Collections.EMPTY_LIST;
    private static final Collection<SchoolSession> SESSIONS = Collections.EMPTY_LIST;

    @Test
    void shouldCreateASchool() {
        // setup the fixture
        School school1 = generateSchool(null);
        UUID id = UUID.randomUUID();
        School school2 = generateSchool(id);

        AwsService awsService = mock(AwsService.class);

        SchoolSessionService schoolSessionService = mock(SchoolSessionService.class);

        SchoolRepository schoolRepository = mock(SchoolRepository.class);
        when(schoolRepository.save(any(School.class))).thenReturn(school2);

        SchoolService schoolService = new SchoolService(awsService, schoolSessionService, schoolRepository);

        // execute the SUT
        School result = schoolService.createSchool(school1);

        // validation
        assertThat(result).isEqualTo(school2);
    }

    @Test
    void shouldFindASchool() {
        // setup the fixture
        UUID id = UUID.randomUUID();
        School school = generateSchool(id);

        AwsService awsService = mock(AwsService.class);

        SchoolSessionService schoolSessionService = mock(SchoolSessionService.class);

        SchoolRepository schoolRepository = mock(SchoolRepository.class);
        when(schoolRepository.findById(any(UUID.class))).thenReturn(Optional.of(school));

        SchoolService schoolService = new SchoolService(awsService, schoolSessionService, schoolRepository);

        // execute the SUT
        Optional<School> result = schoolService.getSchoolById(id);

        // validation
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(school);
    }

    @Test
    void shouldGetAllSchools() {
        // setup the fixture
        School school1 = generateSchool(UUID.randomUUID());
        School school2 = generateSchool(UUID.randomUUID());

        AwsService awsService = mock(AwsService.class);

        SchoolSessionService schoolSessionService = mock(SchoolSessionService.class);

        SchoolRepository schoolRepository = mock(SchoolRepository.class);
        when(schoolRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(school1, school2));

        SchoolService schoolService = new SchoolService(awsService, schoolSessionService, schoolRepository);

        // execute the SUT
        Iterable<School> schools = schoolService.getAllSchools();

        // validation
        assertThat(schools).containsExactly(school1, school2);
    }

    @Test
    void shouldNotFindSchool() {
        // setup the fixture
        UUID id = UUID.randomUUID();

        AwsService awsService = mock(AwsService.class);

        SchoolSessionService schoolSessionService = mock(SchoolSessionService.class);

        SchoolRepository schoolRepository = mock(SchoolRepository.class);
        when(schoolRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        SchoolService schoolService = new SchoolService(awsService, schoolSessionService, schoolRepository);

        // execute the SUT
        Optional<School> result = schoolService.getSchoolById(id);

        // validation
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void shouldRemoveASchool() {
        // setup the fixture
        School school = mock(School.class);

        AwsService awsService = mock(AwsService.class);

        SchoolSessionService schoolSessionService = mock(SchoolSessionService.class);

        SchoolRepository schoolRepository = mock(SchoolRepository.class);
        SchoolService schoolService = new SchoolService(awsService, schoolSessionService, schoolRepository);

        // execute the SUT
        schoolService.deactivateSchool(school);

        // validation
        verify(school).setIsActive(false);
        verify(schoolRepository).save(school);
    }

    @Test
    void shouldUpdateASchool() {
        // setup the fixture
        School school1 = generateSchool(null);
        UUID id = UUID.randomUUID();
        School school2 = generateSchool(id);

        AwsService awsService = mock(AwsService.class);

        SchoolSessionService schoolSessionService = mock(SchoolSessionService.class);

        SchoolRepository schoolRepository = mock(SchoolRepository.class);
        when(schoolRepository.save(any(School.class))).thenReturn(school2);

        SchoolService schoolService = new SchoolService(awsService,
                schoolSessionService, schoolRepository);

        // execute the SUT
        School result = schoolService.updateSchool(school1);

        // validation
        assertThat(result).isEqualTo(school2);
    }

    private School generateSchool(UUID id) {
        return new School(id, NAME, STREET1, STREET2, CITY, STATE, ZIP, PHONE, DISTRICT, IS_PRIVATE, IS_ACTIVE,
                EMAIL_TAG, ROLES, SESSIONS, null, STUDENTS, BOOKS, GAMES);
    }

}
