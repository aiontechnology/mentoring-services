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

import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.repository.LeadershipSkillRepository;
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
public class LeadershipSkillService {

    /** The repository used to interact with the database */
    private final LeadershipSkillRepository leadershipSkillRepository;

    /**
     * Find a {@link LeadershipSkill} by its name.
     *
     * @param name The name of the desired {@link LeadershipSkill}.
     * @return The {@link LeadershipSkill} if it could be found.
     */
    public Optional<LeadershipSkill> findLeadershipSkillByName(String name) {
        return leadershipSkillRepository.findByName(name);
    }

    /**
     * Get all {@link LeadershipSkill LeadershipSkills}.
     *
     * @return All {@link LeadershipSkill LeadershipSkills}.
     */
    public Iterable<LeadershipSkill> getAllLeadershipSkills() {
        return leadershipSkillRepository.findAllByOrderByNameAsc();
    }

}
