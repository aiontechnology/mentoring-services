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

package io.aiontechnology.mentorsuccess.service;

import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing school.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class SchoolService {

    /** Repository for the {@link School} Entity */
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
     * Find a {@link School} by its id.
     *
     * @param id The id of the desired {@link School}.
     * @return The school if it could be found or empty if not.
     */
    public Optional<School> findSchool(UUID id) {
        return schoolRepository.findById(id);
    }

    /**
     * Get all the {@link School Schools} in the database.
     *
     * @return An iterable of all {@link School Schools}.
     */
    public Iterable<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    /**
     * Remove a {@link School} from the system.
     *
     * @param id The id of the {@link School} to remove.
     */
    @Transactional
    public void removeSchool(UUID id) {
        schoolRepository.deleteById(id);
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
