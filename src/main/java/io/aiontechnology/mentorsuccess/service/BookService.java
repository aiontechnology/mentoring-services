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
 * @author <a href="mailto:whitney@aiontechnology.io">Whitney Hunter</a>
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

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
        bookRepository.save(book);
        return book;
    }


    public Optional<Book> getBook(UUID id) {
        return bookRepository.findById(id);
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
