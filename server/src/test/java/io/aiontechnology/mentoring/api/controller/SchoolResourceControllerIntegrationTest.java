/*
 * Copyright 2021-2024 Aion Technology LLC
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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.security.ProgramAdminAuthoritySetter;
import io.aiontechnology.mentoring.security.SystemAdminAuthoritySetter;
import io.aiontechnology.mentoring.util.SchoolUUIDMatcher;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Whitney Hunter
 * @since 1.1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentoring/api/controller/schoolresource-controller.sql"})
@Transactional
public class SchoolResourceControllerIntegrationTest {

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    void testGetSchoolBooks_ProgramAdmin() throws Exception {
        // setup the fixture
        UUID schoolId = UUID.fromString("b61cbdc4-b37e-429d-b33a-108f9753a073");
        School school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        SchoolPersonRole schoolPersonRole = mock(SchoolPersonRole.class);
        when(schoolPersonRole.getSchool()).thenReturn(school);

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/b61cbdc4-b37e-429d-b33a-108f9753a073/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("username", Optional.of(schoolPersonRole))
                        .claim("cognito:groups", new ProgramAdminAuthoritySetter(new SchoolUUIDMatcher()))
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.bookList").isArray())
                .andExpect(jsonPath("$._embedded.bookList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.bookList[*].title",
                        hasItems("TITLE1", "TITLE2")));
    }

    @Test
    void testGetSchoolBooks_SystemAdmin() throws Exception {
        // setup the fixture

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/b61cbdc4-b37e-429d-b33a-108f9753a073/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.bookList").isArray())
                .andExpect(jsonPath("$._embedded.bookList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.bookList[*].title",
                        hasItems("TITLE1", "TITLE2")));
    }

    @Test
    void testGetSchoolGames_ProgramAdmin() throws Exception {
        // setup the fixture
        UUID schoolId = UUID.fromString("b61cbdc4-b37e-429d-b33a-108f9753a073");
        School school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        SchoolPersonRole schoolPersonRole = mock(SchoolPersonRole.class);
        when(schoolPersonRole.getSchool()).thenReturn(school);

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/b61cbdc4-b37e-429d-b33a-108f9753a073/games")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("username", Optional.of(schoolPersonRole))
                        .claim("cognito:groups", new ProgramAdminAuthoritySetter(new SchoolUUIDMatcher()))
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.gameList").isArray())
                .andExpect(jsonPath("$._embedded.gameList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.gameList[*].name",
                        hasItems("GAME1", "GAME2")));
    }

    @Test
    void testGetSchoolGames_SystemAdmin() throws Exception {
        // setup the fixture

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/b61cbdc4-b37e-429d-b33a-108f9753a073/games")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.gameList").isArray())
                .andExpect(jsonPath("$._embedded.gameList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.gameList[*].name",
                        hasItems("GAME1", "GAME2")));
    }

    @Test
    void testSetSchoolBooks_ProgramAdmin() throws Exception {
        // setup the fixture
        UUID schoolId = UUID.fromString("fd03c21f-cd39-4c05-b3f1-6d49618b6b10");
        School school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        SchoolPersonRole schoolPersonRole = mock(SchoolPersonRole.class);
        when(schoolPersonRole.getSchool()).thenReturn(school);

        List<URI> bookURIs = Arrays.asList(
                URI.create("http://localhost:8080/api/v1/books/f53af381-d524-40f7-8df9-3e808c9ad46b"),
                URI.create("http://localhost:8080/api/v1/books/6e4da9bd-5387-45dc-9714-fb96387da770"));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("username", Optional.of(schoolPersonRole))
                        .claim("cognito:groups", new ProgramAdminAuthoritySetter(new SchoolUUIDMatcher()))
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookURIs)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.bookList").isArray())
                .andExpect(jsonPath("$._embedded.bookList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.bookList[*].title",
                        hasItems("TITLE1", "TITLE2")))
                .andExpect(jsonPath("$._embedded.bookList[*]._links.self[0].href",
                        hasItems("http://localhost/api/v1/books/f53af381-d524-40f7-8df9-3e808c9ad46b",
                                "http://localhost/api/v1/books/6e4da9bd-5387-45dc-9714-fb96387da770")));
    }

    @Test
    void testSetSchoolBooks_SystemAdmin() throws Exception {
        // setup the fixture
        List<URI> bookURIs = Arrays.asList(
                URI.create("http://localhost:8080/api/v1/books/f53af381-d524-40f7-8df9-3e808c9ad46b"),
                URI.create("http://localhost:8080/api/v1/books/6e4da9bd-5387-45dc-9714-fb96387da770"));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookURIs)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.bookList").isArray())
                .andExpect(jsonPath("$._embedded.bookList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.bookList[*].title",
                        hasItems("TITLE1", "TITLE2")))
                .andExpect(jsonPath("$._embedded.bookList[*]._links.self[0].href",
                        hasItems("http://localhost/api/v1/books/f53af381-d524-40f7-8df9-3e808c9ad46b",
                                "http://localhost/api/v1/books/6e4da9bd-5387-45dc-9714-fb96387da770")));
    }

    @Test
    void testSetSchoolGames_ProgramAdmin() throws Exception {
        // setup the fixture
        UUID schoolId = UUID.fromString("fd03c21f-cd39-4c05-b3f1-6d49618b6b10");
        School school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        SchoolPersonRole schoolPersonRole = mock(SchoolPersonRole.class);
        when(schoolPersonRole.getSchool()).thenReturn(school);

        List<URI> gameUUIDs = Arrays.asList(
                URI.create("http://localhost:8080/api/v1/games/81b4fd55-0f1d-45d9-9625-9bc3a367fe04"),
                URI.create("http://localhost:8080/api/v1/games/98866d5e-8dd8-433d-bdb5-dbd7fe8dce81"));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/games")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("username", Optional.of(schoolPersonRole))
                        .claim("cognito:groups", new ProgramAdminAuthoritySetter(new SchoolUUIDMatcher()))
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameUUIDs)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.gameList").isArray())
                .andExpect(jsonPath("$._embedded.gameList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.gameList[*].name",
                        hasItems("GAME1", "GAME2")))
                .andExpect(jsonPath("$._embedded.gameList[*]._links.self[0].href",
                        hasItems("http://localhost/api/v1/games/81b4fd55-0f1d-45d9-9625-9bc3a367fe04",
                                "http://localhost/api/v1/games/98866d5e-8dd8-433d-bdb5-dbd7fe8dce81")));
    }

    @Test
    void testSetSchoolGames_SystemAdmin() throws Exception {
        // setup the fixture
        List<URI> gameUUIDs = Arrays.asList(
                URI.create("http://localhost:8080/api/v1/games/81b4fd55-0f1d-45d9-9625-9bc3a367fe04"),
                URI.create("http://localhost:8080/api/v1/games/98866d5e-8dd8-433d-bdb5-dbd7fe8dce81"));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/games")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameUUIDs)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.gameList").isArray())
                .andExpect(jsonPath("$._embedded.gameList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.gameList[*].name",
                        hasItems("GAME1", "GAME2")))
                .andExpect(jsonPath("$._embedded.gameList[*]._links.self[0].href",
                        hasItems("http://localhost/api/v1/games/81b4fd55-0f1d-45d9-9625-9bc3a367fe04",
                                "http://localhost/api/v1/games/98866d5e-8dd8-433d-bdb5-dbd7fe8dce81")));
    }

}
