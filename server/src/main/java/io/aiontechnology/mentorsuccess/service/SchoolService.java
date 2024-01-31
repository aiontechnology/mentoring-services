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

package io.aiontechnology.mentorsuccess.service;

import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.model.enumeration.RoleType;
import io.aiontechnology.mentorsuccess.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for schools.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class SchoolService {

    // Services
    private final AwsService awsService;
    private final SchoolSessionService schoolSessionService;

    // Repositories
    private final SchoolRepository schoolRepository;

    /**
     * Create a new school in the database.
     *
     * @param school The {@link School} entity to put into the database.
     * @return The created {@link School}.
     */
    @Transactional
    public School createSchool(School school) {
        return schoolRepository.save(school);
    }

    /**
     * Deactivate a {@link School} in the system.
     *
     * @param school The {@link School} to deactivate.
     * @return The {@link School} after being deactivated.
     */
    @Transactional
    public School deactivateSchool(School school) {
        school.setIsActive(false);
        schoolRepository.save(school);
        return school;
    }

    /**
     * Get all the {@link School Schools} in the database.
     *
     * @return An iterable of all {@link School Schools}.
     */
    public Iterable<School> getAllSchools() {
        return schoolRepository.findAllByOrderByNameAsc();
    }

    /**
     * Find a {@link School} by its id.
     *
     * @param id The id of the desired {@link School}.
     * @return The school if it could be found or empty if not.
     */
    public Optional<School> getSchoolById(UUID id) {
        return schoolRepository.findById(id);
    }

    @Transactional
    public School removeAllProgramAdministrators(School school) {
        school.getRoles().stream()
                .filter(role -> role.getType().equals(RoleType.PROGRAM_ADMIN))
                .forEach(pa -> awsService.removeAwsUser(pa));
        return school;
    }

    @Transactional
    public School setInitialSession(School school, String initialSessionLabel) {
        if (initialSessionLabel != null) {
            SchoolSession schoolSession = new SchoolSession();
            schoolSession.setLabel(initialSessionLabel);
            schoolSession.setSchool(school);
            school.setCurrentSession(schoolSession);
            schoolSessionService.saveSchoolSession(schoolSession);
        }
        return school;
    }

    /**
     * Update the given {@link School} in the database.
     *
     * @param school The {@link School} to update.
     * @return The updated {@link School}.
     */
    @Transactional
    public School updateSchool(School school) {
        return schoolRepository.save(school);
    }

}
