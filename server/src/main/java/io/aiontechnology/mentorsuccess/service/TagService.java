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

package io.aiontechnology.mentorsuccess.service;

import io.aiontechnology.mentorsuccess.entity.reference.Tag;
import io.aiontechnology.mentorsuccess.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service that provides business logic for tags.
 *
 * @author Whitney Hunter
 * @since 1.13.0
 */
@Service
@RequiredArgsConstructor
public class TagService {

    /** The repository used to interact with the database */
    private final TagRepository tagRepository;

    /**
     * Find a {@link Tag} by its name.
     *
     * @param name The name of the desired {@link Tag}.
     * @return The {@link Tag} if it could be found.
     */
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    /**
     * Get all {@link Tag tags}.
     *
     * @return All {@link Tag tags}.
     */
    public Iterable<Tag> getAllTags() {
        return tagRepository.findAll();
    }

}
