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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.aiontechnology.mentorsuccess.api.model.inbound.GameModel;
import io.aiontechnology.mentorsuccess.entity.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.aiontechnology.mentorsuccess.entity.ResourceLocation.OFFLINE;
import static io.aiontechnology.mentorsuccess.entity.ResourceLocation.ONLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link GameController}.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentorsuccess/api/controller/game-controller.sql"})
@Transactional
public class GameControllerIntegrationTest {

    private static final String NAME = "Monopoly";
    private static final Integer GRADE1 = 3;
    private static final Integer GRADE2 = 4;
    private static final ResourceLocation LOCATION = OFFLINE;

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    void testCreateGame() throws Exception {
        // setup the fixture
        GameModel gameModel = GameModel.builder()
                .withName(NAME)
                .withGrade1(GRADE1)
                .withGrade2(GRADE2)
                .withLocation(LOCATION)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/games")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.grade1", is(GRADE1)))
                .andExpect(jsonPath("$.grade2", is(GRADE2)))
                .andExpect(jsonPath("$.location", is(LOCATION.toString())))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/games/")));
    }

    @Test
    void testCreateGame_nullAllowedFields() throws Exception {
        // setup the fixture
        GameModel gameModel = GameModel.builder()
                .withName(NAME)
                .withGrade1(GRADE1)
                .withGrade2(GRADE2)
                .withLocation(LOCATION)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/games")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.grade1", is(GRADE1)))
                .andExpect(jsonPath("$.grade2", is(GRADE2)))
                .andExpect(jsonPath("$.location", is(OFFLINE.toString())))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/games/")));
    }

    @Test
    void testCreateGame_nullRequiredValues() throws Exception {
        // setup the fixture
        GameModel gameModel = GameModel.builder()
                .withName(null)
                .withGrade1(null)
                .withGrade2(null)
                .withLocation(null)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/games")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameModel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(4)))
                .andExpect(jsonPath("$.error.name", is("A name is required for a game")))
                .andExpect(jsonPath("$.error.grade1", is("A starting grade level is required for a game")))
                .andExpect(jsonPath("$.error.grade2", is("An ending grade level is required for a game")))
                .andExpect(jsonPath("$.error.location", is("A location is required for a game")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/games")));
    }

    @Test
    void testCreateGame_fieldsInvalid() throws Exception {
        // setup the fixture
        GameModel gameModel = GameModel.builder()
                .withName("12345678901234567890123456789012345678901")
                .withGrade1(7)
                .withGrade2(7)
                .withLocation(OFFLINE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/games")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameModel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(3)))
                .andExpect(jsonPath("$.error.name", is("The name of a game can not be longer than 40 characters")))
                .andExpect(jsonPath("$.error.grade1", is("A grade level must be between 1st and 6th")))
                .andExpect(jsonPath("$.error.grade2", is("A grade level must be between 1st and 6th")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/games")));
    }

    @Test
    void testCreateGame_withRelations() throws Exception {
        // setup the fixture
        Map<String, Object> game = new HashMap<>();
        game.put("name", NAME);
        game.put("grade1", GRADE1);
        game.put("grade2", GRADE2);
        game.put("location", LOCATION);
        game.put("leadershipSkills", Arrays.asList("LEADERSHIP_SKILL1"));

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/games")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.grade1", is(GRADE1)))
                .andExpect(jsonPath("$.grade2", is(GRADE2)))
                .andExpect(jsonPath("$.location", is(LOCATION.toString())))
                .andExpect(jsonPath("$.leadershipSkills.length()", is(1)))
                .andExpect(jsonPath("$.leadershipSkills[0].name", is("LEADERSHIP_SKILL1")))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/games/")));
    }

    @Test
    void testGetAllGames() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/games")
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.gameModelList.length()", is(1)))
                .andExpect(jsonPath("$._embedded.gameModelList[0].id", is("f53af381-d524-40f7-8df9-3e808c9ad46b")));
    }

    @Test
    void testGetGameById_found() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/games/f53af381-d524-40f7-8df9-3e808c9ad46b")
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is("f53af381-d524-40f7-8df9-3e808c9ad46b")))
                .andExpect(jsonPath("$.name", is("NAME")))
                .andExpect(jsonPath("$.grade1", is(1)))
                .andExpect(jsonPath("$.grade2", is(2)))
                .andExpect(jsonPath("$.location", is("OFFLINE")))
                .andExpect(jsonPath("$.leadershipSkills.length()", is(1)))
                .andExpect(jsonPath("$.leadershipSkills[0].name", is("LEADERSHIP_SKILL1")))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/games/")));
    }

    @Test
    void testGetGameById_notFound() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/games/d53af381-d524-40f7-8df9-3e808c9ad46b")
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isNotFound());
    }

    @Test
    void testUpdateGame() throws Exception {
        // setup the fixture
        Map<String, Object> updatedGame = new HashMap<>();
        updatedGame.put("name", "NEW_NAME");
        updatedGame.put("grade1", 2);
        updatedGame.put("grade2", 3);
        updatedGame.put("location", ONLINE.toString());
        updatedGame.put("interests", Arrays.asList("INTEREST2"));
        updatedGame.put("leadershipSkills", Arrays.asList("LEADERSHIP_SKILL2"));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/games/f53af381-d524-40f7-8df9-3e808c9ad46b")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedGame)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is("NEW_NAME")))
                .andExpect(jsonPath("$.grade1", is(2)))
                .andExpect(jsonPath("$.grade2", is(3)))
                .andExpect(jsonPath("$.location", is(ONLINE.toString())))
                .andExpect(jsonPath("$.leadershipSkills.length()", is(1)))
                .andExpect(jsonPath("$.leadershipSkills[0].name", is("LEADERSHIP_SKILL2")))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/games/")));
    }

    @Test
    void testDeactivateGame() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(delete("/api/v1/games/f53af381-d524-40f7-8df9-3e808c9ad46b"));

        // validation
        result.andExpect(status().isNoContent());
    }

}
