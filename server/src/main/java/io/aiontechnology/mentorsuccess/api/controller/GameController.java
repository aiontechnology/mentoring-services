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

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentorsuccess.api.assembler.GameModelAssembler;
import io.aiontechnology.mentorsuccess.api.assembler.LinkProvider;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.Game;
import io.aiontechnology.mentorsuccess.model.inbound.InboundGame;
import io.aiontechnology.mentorsuccess.model.outbound.OutboundGame;
import io.aiontechnology.mentorsuccess.service.GameService;
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
    private final GameModelAssembler gameModelAssembler;
    /** {@link LinkProvider} implementation for games. */
    private final LinkProvider<OutboundGame, Game> linkProvider = (gameModel, game) ->
            Arrays.asList(
                    linkTo(GameController.class).slash(game.getId()).withSelfRel()
            );

    /**
     * A REST endpoint for creating new games.
     *
     * @param inboundGame The model that represents the desired new game.
     * @return A model that represents the created game.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OutboundGame createGame(@RequestBody @Valid InboundGame inboundGame) {
        log.debug("Game: {}", inboundGame);
        return Optional.ofNullable(inboundGame)
                .flatMap(gameMapper::map)
                .map(gameService::createGame)
                .map(b -> gameModelAssembler.toModel(b, linkProvider))
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a book"));
    }

    /**
     * A REST endpoint for retrieving all books.
     *
     * @return A collection of {@link InboundGame} instances for all games.
     */
    @GetMapping
    public CollectionModel<OutboundGame> getAllGames() {
        log.debug("Getting all games");
        var games = StreamSupport.stream(gameService.getAllGames().spliterator(), false)
                .map(s -> gameModelAssembler.toModel(s, linkProvider))
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
    public OutboundGame getGame(@PathVariable("gameId") UUID gameId) {
        return gameService.findGameById(gameId)
                .map(game -> gameModelAssembler.toModel(game, linkProvider))
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
    public OutboundGame updateGame(@PathVariable("gameId") UUID gameId, @RequestBody @Valid InboundGame inboundGame) {
        log.debug("Updating book {} with {}", gameId, inboundGame);
        return gameService.findGameById(gameId)
                .flatMap(game -> gameUpdateMapper.map(inboundGame, game))
                .map(gameService::updateGame)
                .map(game -> gameModelAssembler.toModel(game, linkProvider))
                .orElseThrow(() -> new IllegalArgumentException("Unable to update game"));
    }

    /**
     * A REST endpoint for deactivating a specific game.
     *
     * @param gameId The id of the game to deactivate.
     */
    @DeleteMapping("/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateGame(@PathVariable("gameId") UUID gameId) {
        log.debug("Deactivating game: {}", gameId);
        gameService.findGameById(gameId)
                .ifPresent(gameService::deactivateGame);
    }

}