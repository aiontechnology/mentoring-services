/*
 * Copyright 2021-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.controller;

import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.mapping.toentity.misc.UriModelToBookMapper;
import io.aiontechnology.mentoring.api.mapping.toentity.misc.UriModelToGameMapper;
import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.entity.Game;
import io.aiontechnology.mentoring.resource.BookResource;
import io.aiontechnology.mentoring.resource.GameResource;
import io.aiontechnology.mentoring.service.SchoolResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author Whitney Hunter
 * @since 1.1.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}")
@RequiredArgsConstructor
@Slf4j
public class SchoolResourceController {

    private final Assembler<Book, BookResource> bookAssembler;

    private final Assembler<Game, GameResource> gameAssembler;

    private final UriModelToBookMapper uriModelToBookMapper;

    private final UriModelToGameMapper uriModelToGameMapper;

    /** Service with business logic for school resources */
    private final SchoolResourceService schoolResourceService;

    @GetMapping("/books")
    @PreAuthorize("hasAuthority('schoolresources:read')")
    public CollectionModel<BookResource> getSchoolBooks(@PathVariable("schoolId") UUID schoolId) {
        var books = StreamSupport.stream(schoolResourceService.getBooksForSchool(schoolId).spliterator(), false)
                .map(bookAssembler::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return CollectionModel.of(books);
    }

    @GetMapping("/games")
    @PreAuthorize("hasAuthority('schoolresources:read')")
    public CollectionModel<GameResource> getSchoolGames(@PathVariable("schoolId") UUID schoolId) {
        var games = StreamSupport.stream(schoolResourceService.getGamesForSchool(schoolId).spliterator(), false)
                .map(gameAssembler::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return CollectionModel.of(games);
    }

    @PutMapping("/books")
    @PreAuthorize("hasAuthority('schoolresources:update')")
    public CollectionModel<BookResource> setSchoolBooks(@PathVariable("schoolId") UUID schoolId,
            @RequestBody Collection<URI> bookURIs) {
        List<Book> books = bookURIs.stream()
                .map(u -> uriModelToBookMapper.map(u))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        List<BookResource> outboundBooks =
                StreamSupport.stream(schoolResourceService.setBooksForSchool(schoolId, books).spliterator(), false)
                        .map(bookAssembler::map)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(this::addLinks)
                        .collect(Collectors.toList());
        return CollectionModel.of(outboundBooks);
    }

    @PutMapping("/games")
    @PreAuthorize("hasAuthority('schoolresources:update')")
    public CollectionModel<GameResource> setSchoolGames(@PathVariable("schoolId") UUID schoolId,
            @RequestBody Collection<URI> gameURIs) {
        List<Game> games = gameURIs.stream()
                .map(uriModelToGameMapper::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        List<GameResource> outboundGames =
                StreamSupport.stream(schoolResourceService.setGamessForSchool(schoolId, games).spliterator(), false)
                        .map(gameAssembler::map)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(this::addLinks)
                        .collect(Collectors.toList());
        return CollectionModel.of(outboundGames);
    }

    private BookResource addLinks(BookResource resource) {
        Book book = resource.getContent();
        resource.add(linkTo(BookController.class).slash(book.getId()).withSelfRel());
        return resource;
    }

    private GameResource addLinks(GameResource resource) {
        Game game = resource.getContent();
        resource.add(linkTo(GameController.class).slash(game.getId()).withSelfRel());
        return resource;
    }

}
