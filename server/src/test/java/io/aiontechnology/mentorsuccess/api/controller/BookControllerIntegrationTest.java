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

package io.aiontechnology.mentorsuccess.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.aiontechnology.mentorsuccess.model.inbound.InboundBook;
import io.aiontechnology.mentorsuccess.security.SystemAdminAuthoritySetter;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation.ONLINE;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link BookController}.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentorsuccess/api/controller/book-controller.sql"})
@Transactional
public class BookControllerIntegrationTest {

    private static final String TITLE = "The Fellowship of the Rings";
    private static final String AUTHOR = "J. R. R. Tolkien";
    private static final Integer GRADE_LEVEL = 3;

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    void testCreateBook() throws Exception {
        // setup the fixture
        InboundBook inboundBook = InboundBook.builder()
                .withTitle(TITLE)
                .withAuthor(AUTHOR)
                .withGradeLevel(GRADE_LEVEL)
                .withLocation(ONLINE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundBook)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.author", is(AUTHOR)))
                .andExpect(jsonPath("$.gradeLevel", is(GRADE_LEVEL)))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/books/")));
    }

    @Test
    void testCreateBook_fieldsInvalid() throws Exception {
        // setup the fixture
        InboundBook inboundBook = InboundBook.builder()
                .withTitle(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901")
                .withAuthor("1234567890123456789012345678901")
                .withGradeLevel(13)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundBook)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(4)))
                .andExpect(jsonPath("$.error.title", is("The title of a book can not be longer than 100 characters")))
                .andExpect(jsonPath("$.error.author", is("The book's author can not be longer than 30 characters")))
                .andExpect(jsonPath("$.error.gradeLevel", is("A grade level must be between 1st and 6th")))
                .andExpect(jsonPath("$.error.location", is("A location is required for a book")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/books")));
    }

    @Test
    void testCreateBook_nullAllowedFields() throws Exception {
        // setup the fixture
        InboundBook inboundBook = InboundBook.builder()
                .withTitle(TITLE)
                .withAuthor(null)
                .withGradeLevel(GRADE_LEVEL)
                .withLocation(ONLINE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundBook)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.author", nullValue()))
                .andExpect(jsonPath("$.gradeLevel", is(GRADE_LEVEL)))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/books/")));
    }

    @Test
    void testCreateBook_nullRequiredValues() throws Exception {
        // setup the fixture
        InboundBook inboundBook = InboundBook.builder()
                .withTitle(null)
                .withAuthor(AUTHOR)
                .withGradeLevel(null)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundBook)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(3)))
                .andExpect(jsonPath("$.error.title", is("A title is required for a book")))
                .andExpect(jsonPath("$.error.gradeLevel", is("A grade level is required for a book")))
                .andExpect(jsonPath("$.error.location", is("A location is required for a book")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/books")));
    }

    @Test
    void testCreateBook_withRelations() throws Exception {
        // setup the fixture
        Map<String, Object> book = new HashMap<>();
        book.put("title", TITLE);
        book.put("author", AUTHOR);
        book.put("gradeLevel", GRADE_LEVEL);
        book.put("location", ONLINE);
        book.put("interests", Arrays.asList("INTEREST1"));
        book.put("leadershipTraits", Arrays.asList("LEADERSHIP_TRAIT1"));
        book.put("leadershipSkills", Arrays.asList("LEADERSHIP_SKILL1"));
        book.put("phonograms", Arrays.asList("PH1"));
        book.put("behaviors", Arrays.asList("BEHAVIOR1"));
        book.put("tags", Arrays.asList("TAG"));

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.author", is(AUTHOR)))
                .andExpect(jsonPath("$.gradeLevel", is(GRADE_LEVEL)))
                .andExpect(jsonPath("$.interests.length()", is(1)))
                .andExpect(jsonPath("$.interests[0]", is("INTEREST1")))
                .andExpect(jsonPath("$.leadershipTraits.length()", is(1)))
                .andExpect(jsonPath("$.leadershipTraits[0]", is("LEADERSHIP_TRAIT1")))
                .andExpect(jsonPath("$.leadershipSkills.length()", is(1)))
                .andExpect(jsonPath("$.leadershipSkills[0]", is("LEADERSHIP_SKILL1")))
                .andExpect(jsonPath("$.phonograms.length()", is(1)))
                .andExpect(jsonPath("$.phonograms[0]", is("PH1")))
                .andExpect(jsonPath("$.behaviors.length()", is(1)))
                .andExpect(jsonPath("$.behaviors[0]", is("BEHAVIOR1")))
                .andExpect(jsonPath("$.tags[0]", is("TAG")))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/books/")));
    }

    @Test
    void testDeactivateBook() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(delete("/api/v1/books/f53af381-d524-40f7-8df9-3e808c9ad46b")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isNoContent());
    }

    @Test
    void testGetAllBooks() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/books")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bookList.length()", is(1)))
                .andExpect(jsonPath("$._embedded.bookList[0].id", is("f53af381-d524-40f7-8df9-3e808c9ad46b")));
    }

    @Test
    void testGetBookById_found() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/books/f53af381-d524-40f7-8df9-3e808c9ad46b")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is("f53af381-d524-40f7-8df9-3e808c9ad46b")))
                .andExpect(jsonPath("$.title", is("TITLE")))
                .andExpect(jsonPath("$.author", is("AUTHOR")))
                .andExpect(jsonPath("$.gradeLevel", is(1)))
                .andExpect(jsonPath("$.interests.length()", is(1)))
                .andExpect(jsonPath("$.interests[0]", is("INTEREST1")))
                .andExpect(jsonPath("$.leadershipTraits.length()", is(1)))
                .andExpect(jsonPath("$.leadershipTraits[0]", is("LEADERSHIP_TRAIT1")))
                .andExpect(jsonPath("$.leadershipSkills.length()", is(1)))
                .andExpect(jsonPath("$.leadershipSkills[0]", is("LEADERSHIP_SKILL1")))
                .andExpect(jsonPath("$.phonograms.length()", is(2)))
                .andExpect(jsonPath("$.phonograms", hasItems("PH1", "PH1")))
                .andExpect(jsonPath("$.behaviors.length()", is(2)))
                .andExpect(jsonPath("$.behaviors", hasItems("BEHAVIOR1", "BEHAVIOR2")))
                .andExpect(jsonPath("$.tags[0]", is("TAG")))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/books/")));
    }

    @Test
    void testGetBookById_notFound() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/books/d53af381-d524-40f7-8df9-3e808c9ad46b")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBook() throws Exception {
        // setup the fixture
        Map<String, Object> updatedBook = new HashMap<>();
        updatedBook.put("title", "NEW_TITLE");
        updatedBook.put("author", "NEW_AUTHOR");
        updatedBook.put("gradeLevel", 2);
        updatedBook.put("location", ONLINE);
        updatedBook.put("interests", Arrays.asList("INTEREST2"));
        updatedBook.put("leadershipTraits", Arrays.asList("LEADERSHIP_TRAIT2"));
        updatedBook.put("leadershipSkills", Arrays.asList("LEADERSHIP_SKILL2"));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/books/f53af381-d524-40f7-8df9-3e808c9ad46b")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.title", is("NEW_TITLE")))
                .andExpect(jsonPath("$.author", is("NEW_AUTHOR")))
                .andExpect(jsonPath("$.gradeLevel", is(2)))
                .andExpect(jsonPath("$.interests.length()", is(1)))
                .andExpect(jsonPath("$.interests[0]", is("INTEREST2")))
                .andExpect(jsonPath("$.leadershipTraits.length()", is(1)))
                .andExpect(jsonPath("$.leadershipTraits[0]", is("LEADERSHIP_TRAIT2")))
                .andExpect(jsonPath("$.leadershipSkills.length()", is(1)))
                .andExpect(jsonPath("$.leadershipSkills[0]", is("LEADERSHIP_SKILL2")))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/books/")));
    }

}
