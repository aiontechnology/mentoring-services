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

package io.aiontechnology.mentorsuccess.api.controller;

import io.aiontechnology.mentorsuccess.api.assembler.BookModelAssembler;
import io.aiontechnology.mentorsuccess.api.assembler.LinkProvider;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.api.mapping.BookMapper;
import io.aiontechnology.mentorsuccess.api.model.BookModel;
import io.aiontechnology.mentorsuccess.entity.Book;
import io.aiontechnology.mentorsuccess.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Controller that vends a REST interface for dealing with books.
 *
 * @author <a href="mailto:whitney@aiontechnology.io">Whitney Hunter</a>
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    /** Service for interacting with {@link Book Books}. */
    private final BookService bookService;

    /** A mapper between {@link Book Books} and {@link BookModel BookModels}. */
    private final BookMapper bookMapper;

    /** A HATEOAS assembler for {@link BookModel BookModels}. */
    private final BookModelAssembler bookModelAssembler;

    /**
     * A REST endpoint for creating new books.
     *
     * @param bookModel The model that represents the desired new book.
     * @return A model that represents the created book.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookModel createBook(@RequestBody @Valid BookModel bookModel) {
        log.debug("Book: {}", bookModel);
        return Optional.ofNullable(bookModel)
                .map(bookMapper::mapModelToEntity)
                .map(bookService::createBook)
                .map(b -> bookModelAssembler.toModel(b, linkProvider))
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a book"));
    }

    /**
     * A REST endpoint for retrieving all books.
     *
     * @return A collection of {@link BookModel} instances for all books.
     */
    @GetMapping
    public CollectionModel<BookModel> getAllBooks() {
        log.debug("Getting all books");
        var books = StreamSupport.stream(bookService.getAllBooks().spliterator(), false)
                .map(s -> bookModelAssembler.toModel(s, linkProvider))
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
    public BookModel getBook(@PathVariable("bookId") UUID bookId) {
        return bookService.getBook(bookId)
                .map(book -> bookModelAssembler.toModel(book, linkProvider))
                .orElseThrow(() -> new NotFoundException("Book was not found"));
    }

    /**
     * A REST endpoint for updating a specific book.
     *
     * @param bookId The id of the book to update.
     * @param bookModel A model representing the new desired state for the book.
     * @return A model representing the updated book.
     */
    @PutMapping("/{bookId}")
    public BookModel updateBook(@PathVariable("bookId") UUID bookId, @RequestBody @Valid BookModel bookModel) {
        log.debug("Updating book {} with {}", bookId, bookModel);
        return bookService.getBook(bookId)
                .map(book -> bookMapper.mapModelToEntity(bookModel, book))
                .map(bookService::updateBook)
                .map(book -> bookModelAssembler.toModel(book, linkProvider))
                .orElseThrow(() -> new IllegalArgumentException("Unable to update book"));
    }

    /**
     * A REST endpoint for deactivating a specific book.
     *
     * @param bookId The id of the book to deactivate.
     */
    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateBook(@PathVariable("bookId") UUID bookId) {
        log.debug("Deactivating book: {}", bookId);
        bookService.getBook(bookId)
                .ifPresent(bookService::deactivateBook);
    }

    private LinkProvider<BookModel, Book> linkProvider = (bookModel, book) ->
            Arrays.asList(
                    linkTo(BookController.class).slash(book.getId()).withSelfRel()
            );

}
