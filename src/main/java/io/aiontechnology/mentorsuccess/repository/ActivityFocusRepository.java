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

import io.aiontechnology.mentorsuccess.entity.ActivityFocus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * A Spring repository for interacting with {@link ActivityFocus} entities in the database.
 *
 * @author Whitney Hunter
 * @since 0.2.0
 */
@Repository
public interface ActivityFocusRepository extends CrudRepository<ActivityFocus, UUID> {

    /**
     * Find an {@link ActivityFocus} by its name.
     *
     * @param name The name of the desired {@link ActivityFocus}.
     * @return The {@link ActivityFocus} if it could be found.
     */
    Optional<ActivityFocus> findByName(String name);

}
