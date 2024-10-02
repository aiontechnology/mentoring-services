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

package io.aiontechnology.mentoring.api.controller;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.model.inbound.InboundBook;
import io.aiontechnology.mentoring.resource.BookResource;
import io.aiontechnology.mentoring.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller that vends a REST interface for dealing with books.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    // Assemblers
    private final Assembler<Book, BookResource> bookAssembler;

    // Mappers
    private final OneWayMapper<InboundBook, Book> bookMapper;
    private final OneWayUpdateMapper<InboundBook, Book> bookUpdateMapper;

    // Services
    private final BookService bookService;

    /**
     * A REST endpoint for creating new books.
     *
     * @param inboundBook The model that represents the desired new book.
     * @return A model that represents the created book.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('book:create')")
    public BookResource createBook(@RequestBody @Valid InboundBook inboundBook) {
        log.debug("Book: {}", inboundBook);
        return Optional.ofNullable(inboundBook)
                .flatMap(bookMapper::map)
                .map(bookService::createBook)
                .flatMap(bookAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a book"));
    }

    /**
     * A REST endpoint for deactivating a specific book.
     *
     * @param bookId The id of the book to deactivate.
     */
    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('book:delete')")
    public BookResource deactivateBook(@PathVariable("bookId") UUID bookId) {
        log.debug("Deactivating book: {}", bookId);
        return bookService.findBookById(bookId)
                .map(bookService::deactivateBook)
                .flatMap(bookAssembler::map)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    /**
     * A REST endpoint for retrieving all books.
     *
     * @return A collection of {@link InboundBook} instances for all books.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('books:read')")
    public CollectionModel<BookResource> getAllBooks() {
        log.debug("Getting all books");
        var books = StreamSupport.stream(bookService.getAllBooks().spliterator(), false)
                .map(bookAssembler::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return CollectionModel.of(books);
    }

    /**
     * A REST endpoint for getting a book by it's id.
     *
     * @param bookId The id of the desired book.
     * @return A model that represents the book if it could be found.
     */
    @GetMapping("/{bookId}")
    @PreAuthorize("hasAuthority('book:read')")
    public BookResource getBook(@PathVariable("bookId") UUID bookId) {
        return bookService.findBookById(bookId)
                .flatMap(bookAssembler::map)
                .orElseThrow(() -> new NotFoundException("Book was not found"));
    }

    /**
     * A REST endpoint for updating a specific book.
     *
     * @param bookId The id of the book to update.
     * @param inboundBook A model representing the new desired state for the book.
     * @return A model representing the updated book.
     */
    @PutMapping("/{bookId}")
    @PreAuthorize("hasAuthority('book:update')")
    public BookResource updateBook(@PathVariable("bookId") UUID bookId, @RequestBody @Valid InboundBook inboundBook) {
        log.debug("Updating book {} with {}", bookId, inboundBook);
        return bookService.findBookById(bookId)
                .flatMap(book -> bookUpdateMapper.map(inboundBook, book))
                .map(bookService::updateBook)
                .flatMap(bookAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to update book"));
    }

}
