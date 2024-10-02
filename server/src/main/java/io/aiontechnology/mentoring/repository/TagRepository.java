/*
 * Copyright 2021 Aion Technology LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.aiontechnology.mentoring.repository;

import io.aiontechnology.mentoring.entity.reference.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * A Spring repository for interacting with {@link Tag} entities in the database.
 *
 * @author Whitney Hunter
 * @since 1.13.0
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, UUID> {

    /**
     * Find an {@link Tag} by its name.
     *
     * @param name The name of the desired {@link Tag}.
     * @return The {@link Tag} if it could be found.
     */
    Optional<Tag> findByName(String name);

}
