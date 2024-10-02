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

package io.aiontechnology.mentoring.service;

import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for people.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class PersonService {

    /** The repository used to interact with the database */
    private final PersonRepository personRepository;

    /**
     * Create a person in the database by saving the provided {@link Person}.
     *
     * @param person The {@link Person} to save.
     * @return The resulting {@link Person}. Will have a db generated id populated.
     */
    @Transactional
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    /**
     * Find a {@link Person} by its id.
     *
     * @param id The id of the desired {@link Person}.
     * @return The {@link Person} if it could be found.
     */
    public Optional<Person> findPersonById(UUID id) {
        return personRepository.findById(id);
    }

}
