/*
 * Copyright 2020-2022 Aion Technology LLC
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

import io.aiontechnology.mentorsuccess.entity.Book;
import io.aiontechnology.mentorsuccess.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for books.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    /** The repository used to interact with the database */
    private final BookRepository bookRepository;

    /**
     * Create a book in the database by saving the provided {@link Book}.
     *
     * @param book The {@link Book} to save.
     * @return The resulting {@link Book}. Will have a db generated id populated.
     */
    @Transactional
    public Book createBook(Book book) {
        log.debug("Creating book: {}", book);
        return bookRepository.save(book);
    }

    /**
     * Deactivate a {@link Book} in the system.
     *
     * @param book The {@link Book} to deactivate.
     */
    @Transactional
    public Book deactivateBook(Book book) {
        book.setIsActive(false);
        return bookRepository.save(book);
    }

    /**
     * Find a {@link Book} by its id.
     *
     * @param id The id of the desired {@link Book}.
     * @return The {@link Book} if it could be found.
     */
    public Optional<Book> findBookById(UUID id) {
        return bookRepository.findById(id);
    }

    /**
     * Get all {@link Book Books}.
     *
     * @return All {@link Book Books}.
     */
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAllByOrderByTitleAsc();
    }

    /**
     * Update the given {@link Book} in the database.
     *
     * @param book The {@link Book} to update.
     * @return The updated {@link Book}.
     */
    @Transactional
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

}
