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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.aiontechnology.mentoring.model.enumeration.RoleType;
import io.aiontechnology.mentoring.model.inbound.InboundPersonnel;
import io.aiontechnology.mentoring.security.SystemAdminAuthoritySetter;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static io.aiontechnology.mentoring.model.enumeration.RoleType.SOCIAL_WORKER;
import static io.aiontechnology.mentoring.model.enumeration.RoleType.TEACHER;
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
 * Tests for {@link PersonnelController}
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentoring/api/controller/personnel-controller.sql"})
@Transactional
public class PersonnelControllerIntegrationTest {

    private static final RoleType TYPE = SOCIAL_WORKER;
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
    void testCreatePersonnel() throws Exception {
        // setup the fixture
        InboundPersonnel inboundPersonnel = InboundPersonnel.builder()
                .withType(TYPE)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail(EMAIL)
                .withWorkPhone(WORK_PHONE)
                .withCellPhone(CELL_PHONE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPersonnel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.type", is(TYPE.name())))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.workPhone", is(WORK_PHONE)))
                .andExpect(jsonPath("$.cellPhone", is(CELL_PHONE)))
                .andExpect(jsonPath("$._links.length()", is(2)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/fd03c21f" +
                        "-cd39-4c05-b3f1-6d49618b6b10/personnel/")))
                .andExpect(jsonPath("$._links.school[0].href", is("http://localhost/api/v1/schools/fd03c21f-cd39-4c05" +
                        "-b3f1-6d49618b6b10")));
    }

    @Test
    void testCreatePersonnel_emailInvalid() throws Exception {
        // setup the fixture
        InboundPersonnel inboundPersonnel = InboundPersonnel.builder()
                .withType(TYPE)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail("invalid")
                .withWorkPhone(WORK_PHONE)
                .withCellPhone(CELL_PHONE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPersonnel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.email", is("The provided personnel's email is invalid or longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")));
    }

    @Test
    void testCreatePersonnel_fieldsInvalid() throws Exception {
        // setup the fixture
        InboundPersonnel inboundPersonnel = InboundPersonnel.builder()
                .withType(TEACHER)
                .withFirstName("123456789012345678901234567890123456789012345678901")
                .withLastName("123456789012345678901234567890123456789012345678901")
                .withEmail("123456789012345678901234567890123456789012345678901")
                .withWorkPhone("12345678901")
                .withCellPhone("12345678901")
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPersonnel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(6)))
                .andExpect(jsonPath("$.error.type", is("A personnel's type must be SOCIAL_WORKER, PRINCIPAL, " +
                        "COUNSELOR or STAFF")))
                .andExpect(jsonPath("$.error.firstName", is("A personnel's first name can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.lastName", is("A personnel's last name can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.email", is("The provided personnel's email is invalid or longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.cellPhone", is("The provided personnel's cell phone must be exactly 14 " +
                        "digits")))
                .andExpect(jsonPath("$.error.workPhone", is("The provided personnel's work phone must be exactly 14 " +
                        "digits")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")));
    }

    @Test
    void testCreatePersonnel_nullAllowedFields() throws Exception {
        // setup the fixture
        InboundPersonnel inboundPersonnel = InboundPersonnel.builder()
                .withType(TYPE)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail(null)
                .withWorkPhone(null)
                .withCellPhone(null)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPersonnel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.type", is(TYPE.name())))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.email", nullValue()))
                .andExpect(jsonPath("$.workPhone", nullValue()))
                .andExpect(jsonPath("$.cellPhone", nullValue()))
                .andExpect(jsonPath("$._links.length()", is(2)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/fd03c21f" +
                        "-cd39-4c05-b3f1-6d49618b6b10/personnel/")))
                .andExpect(jsonPath("$._links.school[0].href", is("http://localhost/api/v1/schools/fd03c21f-cd39-4c05" +
                        "-b3f1-6d49618b6b10")));
    }

    @Test
    void testCreatePersonnel_nullRequiredValues() throws Exception {
        // setup the fixture
        InboundPersonnel inboundPersonnel = InboundPersonnel.builder()
                .withType(null)
                .withFirstName(null)
                .withLastName(null)
                .withEmail(EMAIL)
                .withWorkPhone(WORK_PHONE)
                .withCellPhone(CELL_PHONE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundPersonnel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(3)))
                .andExpect(jsonPath("$.error.type", is("A personnel must have a type")))
                .andExpect(jsonPath("$.error.firstName", is("A personnel must have a first name")))
                .andExpect(jsonPath("$.error.lastName", is("A personnel must have a last name")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")));
    }

    @Test
    void testDeactivatePersonnel() throws Exception {
        // setup the fixture

        // execute the SUT
        ResultActions result = mvc.perform(delete("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel" +
                "/ca238442-ce51-450d-a474-2e36872abe05")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isNoContent());
    }

    @Test
    void testGetAllPersonnel() throws Exception {
        // setup the fixture

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk());
    }

    @Test
    void testGetPersonnelById_found() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel" +
                "/ba238442-ce51-450d-a474-2e36872abe05")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("SOCIAL_WORKER")))
                .andExpect(jsonPath("$.firstName", is("Fred")))
                .andExpect(jsonPath("$.lastName", is("Rogers")))
                .andExpect(jsonPath("$.email", is("fred@rogers.com")))
                .andExpect(jsonPath("$.workPhone", is("(360) 111-2222")))
                .andExpect(jsonPath("$.cellPhone", is("(360) 333-4444")))
                .andExpect(jsonPath("$._links.length()", is(2)))
                .andExpect(jsonPath("$._links.self[0].href", is("http://localhost/api/v1/schools/fd03c21f-cd39-4c05" +
                        "-b3f1-6d49618b6b10/personnel/ba238442-ce51-450d-a474-2e36872abe05")))
                .andExpect(jsonPath("$._links.school[0].href", is("http://localhost/api/v1/schools/fd03c21f-cd39-4c05" +
                        "-b3f1-6d49618b6b10")));

    }

    @Test
    void testGetPersonnelById_notFound() throws Exception {
        // setup the fixture
        // None

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel" +
                "/ca238442-ce51-450d-a474-2e36872abe05")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isNotFound());
    }

    @Test
    void testUpdatePersonnel() throws Exception {
        // setup the fixture
        Map<String, Object> personnelModel = new HashMap<>();
        personnelModel.put("type", "PRINCIPAL");
        personnelModel.put("firstName", "NEW FIRST NAME");
        personnelModel.put("lastName", "NEW LAST NAME");
        personnelModel.put("email", "new@email.com");
        personnelModel.put("workPhone", "(360) 765-4321");
        personnelModel.put("cellPhone", "(360) 765-4322");

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/personnel" +
                "/ba238442-ce51-450d-a474-2e36872abe05")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personnelModel)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.type", is("PRINCIPAL")))
                .andExpect(jsonPath("$.firstName", is("NEW FIRST NAME")))
                .andExpect(jsonPath("$.lastName", is("NEW LAST NAME")))
                .andExpect(jsonPath("$.email", is("new@email.com")))
                .andExpect(jsonPath("$.workPhone", is("(360) 765-4321")))
                .andExpect(jsonPath("$.cellPhone", is("(360) 765-4322")))
                .andExpect(jsonPath("$._links.length()", is(2)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/fd03c21f" +
                        "-cd39-4c05-b3f1-6d49618b6b10/personnel/")))
                .andExpect(jsonPath("$._links.school[0].href", is("http://localhost/api/v1/schools/fd03c21f-cd39-4c05" +
                        "-b3f1-6d49618b6b10")));
    }

}
