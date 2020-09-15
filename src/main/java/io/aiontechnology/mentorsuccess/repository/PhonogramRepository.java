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

import io.aiontechnology.mentorsuccess.entity.Phonogram;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * A Spring repository for interacting with {@link Phonogram} entities in the database.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Repository
public interface PhonogramRepository extends CrudRepository<Phonogram, UUID> {

    /**
     * Find an {@link Phonogram} by its name.
     *
     * @param name The name of the desired {@link Phonogram}.
     * @return The {@link Phonogram} if it could be found.
     */
    @Cacheable("phonograms")
    Optional<Phonogram> findByName(String name);

    /**
     * Find all {@link Phonogram Phonograms} in ascending order by name.
     *
     * @return The {@link Phonogram Phonograms}.
     */
    @Cacheable("phonograms")
    Iterable<Phonogram> findAllByOrderByNameAsc();

}
