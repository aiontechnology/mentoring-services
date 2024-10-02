/*
 * Copyright 2022-2024 Aion Technology LLC
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
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import io.aiontechnology.mentoring.model.inbound.InboundSchoolSession;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentSchoolSession;
import io.aiontechnology.mentoring.security.SystemAdminAuthoritySetter;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
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
import java.util.Collection;
import java.util.Date;

import static io.aiontechnology.mentoring.model.enumeration.ResourceLocation.OFFLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentoring/api/controller/schoolsession-controller.sql"})
@Transactional
public class SchoolSessionControllerIntegrationTest {

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    void testCreateSchoolSession() throws Exception {
        // setup the fixture
        String label = "2024-2024";
        InboundSchoolSession schoolSessionModel = InboundSchoolSession.builder()
                .withLabel(label)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/schoolsessions")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schoolSessionModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.label", is(label)));
    }

    @Test
    @Disabled("Fix this")
    void testCreateStudentSchoolSession() throws Exception {
        // setup the fixture
        final URI STUDENT_URI = URI.create(
                "http://localhost:8080/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students/2a8c5871-a21d" +
                        "-47a1-a516-a6376a6b8bf2");
        final Integer GRADE = 4;
        final String PREFERRED_TIME = "1:00pm";
        final String ACTUAL_TIME = "2:00pm";
        final Date START_DATE = new Date();
        final ResourceLocation LOCATION = OFFLINE;
        final Boolean MEDIA_RELEASE_SIGNED = false;
        InboundStudentSchoolSession studentSchoolSession = InboundStudentSchoolSession.builder()
                .withStudent(STUDENT_URI)
                .withGrade(GRADE)
                .withPreferredTime(PREFERRED_TIME)
                .withActualTime(ACTUAL_TIME)
                .withStartDate(START_DATE)
                .withLocation(LOCATION)
                .withMediaReleaseSigned(MEDIA_RELEASE_SIGNED)
                .build();
        Collection<InboundStudentSchoolSession> inboundStudentSchoolSessions = Arrays.asList(studentSchoolSession);

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/schoolsessions" +
                "/e618f863-da09-4648-8098-5e0a8b21ff1f/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundStudentSchoolSessions)));

        // validation
        result.andExpect(status().isOk());
    }

    @Test
    void testDeleteSchoolSession() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(delete("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10" +
                "/schoolsessions/e618f863-da09-4648-8098-5e0a8b21ff1f")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isNoContent());
    }

    @Test
    void testGetSchoolSession() throws Exception {
        // setup the fixture

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/schoolsessions" +
                "/e618f863-da09-4648-8098-5e0a8b21ff1f")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.label", is("2021-2022")));
    }

    @Test
    void testGetSchoolSessions() throws Exception {
        // setup the fixture

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/schoolsessions")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._embedded.schoolSessionList").isArray())
                .andExpect(jsonPath("$._embedded.schoolSessionList.length()", is(2)));
    }

}
