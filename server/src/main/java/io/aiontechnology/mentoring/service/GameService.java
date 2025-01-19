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

import io.aiontechnology.mentoring.entity.Game;
import io.aiontechnology.mentoring.repository.GameRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service that provides business logic for games.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    /** The repository used to interact with the database */
    private final GameRepository gameRepository;

    /**
     * Create a game in the database by saving the provided {@link Game}.
     *
     * @param game The {@link Game} to save.
     * @return The resulting {@link Game}. Will have a db generated id populated.
     */
    @Transactional
    public Game createGame(Game game) {
        log.debug("Creating game: {}", game);
        return gameRepository.save(game);
    }

    /**
     * Deactivate a {@link Game} in the system.
     *
     * @param game The {@link Game} to deactivate.
     */
    @Transactional
    public Game deactivateGame(Game game) {
        game.setIsActive(false);
        return gameRepository.save(game);
    }

    /**
     * Find a {@link Game} by its id.
     *
     * @param id The id of the desired {@link Game}.
     * @return The {@link Game} if it could be found.
     */
    public Optional<Game> findGameById(UUID id) {
        return gameRepository.findById(id);
    }

    /**
     * Get all {@link Game Games} in the system.
     *
     * @return All {@link Game Games}.
     */
    public Iterable<Game> getAllGames() {
        return gameRepository.findAllByOrderByNameAsc();
    }

    /**
     * Update the given {@link Game} in the database.
     *
     * @param Game The {@link Game} to update.
     * @return The updated {@link Game}.
     */
    @Transactional
    public Game updateGame(Game Game) {
        return gameRepository.save(Game);
    }

}
