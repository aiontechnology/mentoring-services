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
import io.aiontechnology.mentorsuccess.model.inbound.InboundPerson;
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
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link PersonController}
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentorsuccess/api/controller/person-controller.sql"})
@Transactional
public class PersonControllerIntegrationTest {

    private static final String FIRST_NAME = "Fred";
    private static final String LAST_NAME = "Rogers";
    private static final String EMAIL = "fred@rogers.com";
    private static final String WORK_PHONE = "(360) 111-2222";
    private static final String CELL_PHONE = "(360) 333-4444";

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    void testCreatePerson() throws Exception {
        // setup the fixture
        InboundPerson inboundPerson = InboundPerson.builder()
                .withId(UUID.randomUUID())
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail(EMAIL)
                .withWorkPhone(WORK_PHONE)
                .withCellPhone(CELL_PHONE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/people")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPerson)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.workPhone", is(WORK_PHONE)))
                .andExpect(jsonPath("$.cellPhone", is(CELL_PHONE)))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/people/")));
    }

    @Test
    void testCreatePerson_emailInvalid() throws Exception {
        // setup the fixture
        InboundPerson inboundPerson = InboundPerson.builder()
                .withId(UUID.randomUUID())
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail("invalid")
                .withWorkPhone(WORK_PHONE)
                .withCellPhone(CELL_PHONE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/people")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPerson)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.email", is("The provided person's email is invalid or longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/people")));
    }

    @Test
    void testCreatePerson_fieldsInvalid() throws Exception {
        // setup the fixture
        InboundPerson inboundPerson = InboundPerson.builder()
                .withId(UUID.randomUUID())
                .withFirstName("123456789012345678901234567890123456789012345678901")
                .withLastName("123456789012345678901234567890123456789012345678901")
                .withEmail("123456789012345678901234567890123456789012345678901")
                .withWorkPhone("12345678901")
                .withCellPhone("12345678901")
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/people")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPerson)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(5)))
                .andExpect(jsonPath("$.error.firstName", is("A person's first name can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.lastName", is("A person's last name can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.email", is("The provided person's email is invalid or longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.cellPhone", is("The provided person's cell phone must be exactly 14 " +
                        "digits")))
                .andExpect(jsonPath("$.error.workPhone", is("The provided person's work phone must be exactly 14 " +
                        "digits")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/people")));
    }

    @Test
    void testCreatePerson_nullAllowedFields() throws Exception {
        // setup the fixture
        InboundPerson inboundPerson = InboundPerson.builder()
                .withId(UUID.randomUUID())
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail(null)
                .withWorkPhone(null)
                .withCellPhone(null)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/people")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPerson)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.email", nullValue()))
                .andExpect(jsonPath("$.workPhone", nullValue()))
                .andExpect(jsonPath("$.cellPhone", nullValue()))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/people/")));
    }

    @Test
    void testCreatePerson_nullRequiredValues() throws Exception {
        // setup the fixture
        InboundPerson inboundPerson = InboundPerson.builder()
                .withId(UUID.randomUUID())
                .withFirstName(null)
                .withLastName(null)
                .withEmail(EMAIL)
                .withWorkPhone(WORK_PHONE)
                .withCellPhone(CELL_PHONE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/people")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPerson)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(2)))
                .andExpect(jsonPath("$.error.firstName", is("A person must have a first name")))
                .andExpect(jsonPath("$.error.lastName", is("A person must have a last name")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/people")));
    }

    @Test
    void testGetPersonById_found() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/people/2f10e8ac-9ad6-4771-a034-ca1d6c387b9b")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is("2f10e8ac-9ad6-4771-a034-ca1d6c387b9b")))
                .andExpect(jsonPath("$.firstName", is("Fred")))
                .andExpect(jsonPath("$.lastName", is("Rogers")))
                .andExpect(jsonPath("$.email", is("fred@rogers.com")))
                .andExpect(jsonPath("$.workPhone", is("(360) 111-2222")))
                .andExpect(jsonPath("$.cellPhone", is("(360) 333-4444")))
                .andExpect(jsonPath("$._links.length()", is(1)))
                .andExpect(jsonPath("$._links.self[0].href", is("http://localhost/api/v1/people/2f10e8ac-9ad6-4771" +
                        "-a034-ca1d6c387b9b")));
    }

    @Test
    void testGetPersonById_notFound() throws Exception {
        // setup the fixture
        // None

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/people/3f10e8ac-9ad6-4771-a034-ca1d6c387b9b")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isNotFound());
    }

}
