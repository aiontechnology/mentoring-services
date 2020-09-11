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

package io.aiontechnology.mentorsuccess.api.assembler;

import io.aiontechnology.mentorsuccess.api.controller.GameController;
import io.aiontechnology.mentorsuccess.api.mapping.GameMapper;
import io.aiontechnology.mentorsuccess.api.model.GameModel;
import io.aiontechnology.mentorsuccess.entity.Game;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Assembles a {@link GameModel} from a {@link Game}.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Component
public class GameModelAssembler extends RepresentationModelAssemblerSupport<Game, GameModel> {

    /** Mapper to map between {@link Game} and {@link GameModel}. */
    private final GameMapper gameMapper;

    /** A utility class for adding links to a model object. */
    private final LinkHelper<GameModel> linkHelper;

    /**
     * Constructor
     *
     * @param gameMapper The mapper for mapping between {@link Game} and {@link GameModel}.
     * @param linkHelper A utility class for adding links to a model object.
     */
    @Inject
    public GameModelAssembler(GameMapper gameMapper, LinkHelper<GameModel> linkHelper) {
        super(GameController.class, GameModel.class);
        this.gameMapper = gameMapper;
        this.linkHelper = linkHelper;
    }

    /**
     * Map a {@link Game} to a {@link GameModel} without adding links.
     *
     * @param game The {@link Game} to map.
     * @return The resulting {@link GameModel}.
     */
    @Override
    public GameModel toModel(Game game) {
        return Optional.ofNullable(game)
                .map(gameMapper::mapEntityToModel)
                .orElse(null);
    }

    /**
     * Map a {@link Game} to a {@link GameModel} and add links.
     *
     * @param game The {@link Game} to map.
     * @param linkProvider An object that provides links.
     * @return The resulting {@link GameModel}.
     */
    public GameModel toModel(Game game, LinkProvider<GameModel, Game> linkProvider) {
        return Optional.ofNullable(game)
                .map(this::toModel)
                .map(model -> linkHelper.addLinks(model, linkProvider.apply(model, game)))
                .orElse(null);
    }

}
