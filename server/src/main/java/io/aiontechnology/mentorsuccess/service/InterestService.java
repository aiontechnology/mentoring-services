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

import io.aiontechnology.mentorsuccess.entity.reference.Interest;
import io.aiontechnology.mentorsuccess.repository.InterestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for interests.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InterestService {

    private static final String CREATE_ME_TOKEN = "CREATE_ME";

    /** The repository used to interact with the database */
    private final InterestRepository interestRepository;

    @Transactional
    public Interest createInterest(Interest interest) {
        return interestRepository.save(interest);
    }

    /**
     * Find an {@link Interest} by its id.
     *
     * @param id The id of the desired {@link Interest}.
     * @return The {@link Interest} if it could be found.
     */
    public Optional<Interest> findInterestById(UUID id) {
        return interestRepository.findById(id);
    }

    /**
     * Find an {@link Interest} by its name.
     *
     * @param name The name of the desired {@link Interest}.
     * @return The {@link Interest} if it could be found.
     */
    public Optional<Interest> findInterestByName(String name) {
        return interestRepository.findByName(name);
    }

    /**
     * Get all {@link Interest Interests}.
     *
     * @return All {@link Interest Interests}.
     */
    public Iterable<Interest> getAllInterests() {
        return interestRepository.findAllByOrderByNameAsc();
    }

    @Transactional
    public Interest updateInterest(Interest interest) {
        return interestRepository.save(interest);
    }

}
