/*
 * Copyright 2020-2023 Aion Technology LLC
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

import io.aiontechnology.mentoring.entity.reference.ActivityFocus;
import io.aiontechnology.mentoring.repository.ActivityFocusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for activity focuses.
 *
 * @author Whitney Hunter
 * @since 0.2.0
 */
@Service
@RequiredArgsConstructor
public class ActivityFocusService {

    /** The repository used to interact with the database */
    private final ActivityFocusRepository activityFocusRepository;

    /**
     * Find an {@link ActivityFocus} by its id.
     *
     * @param id The id of the desired {@link ActivityFocus}.
     * @return The {@link ActivityFocus} if it could be found.
     */
    public Optional<ActivityFocus> findActivityFocusById(UUID id) {
        return activityFocusRepository.findById(id);
    }

    /**
     * Find an {@link ActivityFocus} by its name.
     *
     * @param name The name of the desired {@link ActivityFocus}.
     * @return The {@link ActivityFocus} if it could be found.
     */
    public Optional<ActivityFocus> findActivityFocusByName(String name) {
        return activityFocusRepository.findByName(name);
    }

    /**
     * Get all {@link ActivityFocus ActivityFocuses}.
     *
     * @return All {@link ActivityFocus ActivityFocuses}.
     */
    public Iterable<ActivityFocus> getAllActivityFocuses() {
        return activityFocusRepository.findAll();
    }

}
