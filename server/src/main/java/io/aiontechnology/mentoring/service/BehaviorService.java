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

package io.aiontechnology.mentoring.service;

import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.repository.BehaviorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for behaviors.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class BehaviorService {

    /** The repository used to interact with the database */
    private final BehaviorRepository behaviorRepository;

    /**
     * Find a {@link Behavior} by its id.
     *
     * @param id The id of the desired {@link Behavior}.
     * @return The {@link Behavior} if it could be found.
     */
    public Optional<Behavior> findBehaviorById(UUID id) {
        return behaviorRepository.findById(id);
    }

    /**
     * Find a {@link Behavior} by its name.
     *
     * @param name The name of the desired {@link Behavior}.
     * @return The {@link Behavior} if it could be found.
     */
    public Optional<Behavior> findBehaviorByName(String name) {
        return behaviorRepository.findByName(name);
    }

    /**
     * Get all {@link Behavior Behaviors}.
     *
     * @return All {@link Behavior Behaviors}.
     */
    public Iterable<Behavior> getAllBehaviors() {
        return behaviorRepository.findAllByOrderByNameAsc();
    }

}
