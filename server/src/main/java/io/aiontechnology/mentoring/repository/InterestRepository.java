/*
 * Copyright 2020-2021 Aion Technology LLC
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

package io.aiontechnology.mentoring.repository;

import io.aiontechnology.mentoring.entity.reference.Interest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * A Spring repository for interacting with {@link Interest} entities in the database.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Repository
public interface InterestRepository extends CrudRepository<Interest, UUID> {

    /**
     * Find an {@link Interest} by its name.
     *
     * @param name The name of the desired {@link Interest}.
     * @return The {@link Interest} if it could be found.
     */
//    @Cacheable("interests")
    Optional<Interest> findByName(String name);

    Iterable<Interest> findAllByOrderByNameAsc();

}
