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

import io.aiontechnology.mentoring.entity.reference.Phonogram;
import io.aiontechnology.mentoring.repository.PhonogramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for phonograms.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class PhonogramService {

    /** The repository used to interact with the database */
    private final PhonogramRepository phonogramRepository;

    /**
     * Find a {@link Phonogram} by its id.
     *
     * @param id The id of the desired {@link Phonogram}.
     * @return The {@link Phonogram} if it could be found.
     */
    public Optional<Phonogram> findPhonogramById(UUID id) {
        return phonogramRepository.findById(id);
    }

    /**
     * Find a {@link Phonogram} by its name.
     *
     * @param name The name of the desired {@link Phonogram}.
     * @return The {@link Phonogram} if it could be found.
     */
    public Optional<Phonogram> findPhonogramByName(String name) {
        return phonogramRepository.findByName(name);
    }

    /**
     * Get all {@link Phonogram Phonograms}.
     *
     * @return All {@link Phonogram Phonograms}.
     */
    public Iterable<Phonogram> getAllPhonograms() {
        return phonogramRepository.findAllByOrderByNameAsc();
    }

}
