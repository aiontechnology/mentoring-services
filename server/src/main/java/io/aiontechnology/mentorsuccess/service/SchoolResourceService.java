/*
 * Copyright 2021-2024 Aion Technology LLC
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

import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.Book;
import io.aiontechnology.mentorsuccess.entity.Game;
import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.repository.SchoolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Whitney Hunter
 * @since 1.1.0
 */
@Service
@RequiredArgsConstructor
public class SchoolResourceService {

    /** The repository used to interact with the database */
    private final SchoolRepository schoolRepository;

    public Iterable<Book> getBooksForSchool(UUID schoolId) {
        return schoolRepository.findById(schoolId)
                .map(School::getBooks)
                .orElse(Collections.emptyList());
    }

    public Iterable<Game> getGamesForSchool(UUID schoolId) {
        return schoolRepository.findById(schoolId)
                .map(School::getGames)
                .orElse(Collections.emptyList());
    }

    @Transactional
    public Iterable<Book> setBooksForSchool(UUID schoolId, Collection<Book> books) {
        return schoolRepository.findById(schoolId)
                .map(school -> {
                    school.setBooks(books);
                    return school.getBooks();
                })
                .orElseThrow(() -> new NotFoundException("Unable to find school with id: " + schoolId));
    }

    @Transactional
    public Iterable<Game> setGamessForSchool(UUID schoolId, Collection<Game> games) {
        return schoolRepository.findById(schoolId)
                .map(school -> {
                    school.setGames(games);
                    return school.getGames();
                })
                .orElseThrow(() -> new NotFoundException("Unable to find school with id: " + schoolId));
    }

}
