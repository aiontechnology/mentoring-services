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
import io.aiontechnology.mentoring.entity.Game;
import io.aiontechnology.mentoring.model.inbound.InboundGame;
import io.aiontechnology.mentoring.resource.GameResource;
import io.aiontechnology.mentoring.service.GameService;
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
 * Controller that vends a REST interface for dealing with games.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    /** Service for interacting with {@link Game Games}. */
    private final GameService gameService;

    /** A mapper between {@link InboundGame GameModels} and {@link Game Games}. */
    private final OneWayMapper<InboundGame, Game> gameMapper;

    /** An update mapper between {@link InboundGame GameModels} and {@link Game Games}. */
    private final OneWayUpdateMapper<InboundGame, Game> gameUpdateMapper;

    /** A HATEOAS assembler for {@link InboundGame GameModels}. */
    private final Assembler<Game, GameResource> gameAssembler;

    /**
     * A REST endpoint for creating new games.
     *
     * @param inboundGame The model that represents the desired new game.
     * @return A model that represents the created game.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('game:create')")
    public GameResource createGame(@RequestBody @Valid InboundGame inboundGame) {
        log.debug("Game: {}", inboundGame);
        return Optional.ofNullable(inboundGame)
                .flatMap(gameMapper::map)
                .map(gameService::createGame)
                .flatMap(gameAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a book"));
    }

    /**
     * A REST endpoint for deactivating a specific game.
     *
     * @param gameId The id of the game to deactivate.
     */
    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('game:delete')")
    public GameResource deactivateGame(@PathVariable("gameId") UUID gameId) {
        log.debug("Deactivating game: {}", gameId);
        return gameService.findGameById(gameId)
                .map(gameService::deactivateGame)
                .flatMap(gameAssembler::map)
                .orElseThrow(() -> new NotFoundException("Game not found"));
    }

    /**
     * A REST endpoint for retrieving all books.
     *
     * @return A collection of {@link InboundGame} instances for all games.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('games:read')")
    public CollectionModel<GameResource> getAllGames() {
        log.debug("Getting all games");
        var games = StreamSupport.stream(gameService.getAllGames().spliterator(), false)
                .map(gameAssembler::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return CollectionModel.of(games);
    }

    /**
     * A REST endpoint for getting a game by it's id.
     *
     * @param gameId The id of the desired game.
     * @return A model that represents the game if it could be found.
     */
    @GetMapping("/{gameId}")
    @PreAuthorize("hasAuthority('game:read')")
    public GameResource getGame(@PathVariable("gameId") UUID gameId) {
        return gameService.findGameById(gameId)
                .flatMap(gameAssembler::map)
                .orElseThrow(() -> new NotFoundException("Game was not found"));
    }

    /**
     * A REST endpoint for updating a specific game.
     *
     * @param gameId The id of the game to update.
     * @param inboundGame A model representing the new desired state for the game.
     * @return A model representing the updated game.
     */
    @PutMapping("/{gameId}")
    @PreAuthorize("hasAuthority('game:update')")
    public GameResource updateGame(@PathVariable("gameId") UUID gameId, @RequestBody @Valid InboundGame inboundGame) {
        log.debug("Updating book {} with {}", gameId, inboundGame);
        return gameService.findGameById(gameId)
                .flatMap(game -> gameUpdateMapper.map(inboundGame, game))
                .map(gameService::updateGame)
                .flatMap(gameAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to update game"));
    }

}
