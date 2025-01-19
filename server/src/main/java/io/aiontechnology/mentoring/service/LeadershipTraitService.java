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

package io.aiontechnology.mentoring.service;

import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.repository.LeadershipTraitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service that provides business logic for leadership skills.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class LeadershipTraitService {

    /** The repository used to interact with the database */
    private final LeadershipTraitRepository leadershipTraitRepository;

    /**
     * Find a {@link LeadershipTrait} by its name.
     *
     * @param name The name of the desired {@link LeadershipTrait}.
     * @return The {@link LeadershipTrait} if it could be found.
     */
    public Optional<LeadershipTrait> findLeadershipTraitByName(String name) {
        return leadershipTraitRepository.findByName(name);
    }

    /**
     * Get all {@link LeadershipTrait LeadershipTraits}.
     *
     * @return All {@link LeadershipTrait LeadershipTraits}.
     */
    public Iterable<LeadershipTrait> getAllLeadershipTraits() {
        return leadershipTraitRepository.findAllByOrderByNameAsc();
    }

}
