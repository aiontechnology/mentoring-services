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
import io.aiontechnology.mentorsuccess.model.inbound.InboundAddress;
import io.aiontechnology.mentorsuccess.model.inbound.InboundSchool;
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
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.endsWith;
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
 * Tests for {@link SchoolController}
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentorsuccess/api/controller/school-controller.sql"})
@Transactional
public class SchoolControllerIntegrationTest {

    private static final String NAME = "TEST";
    private static final String STREET1 = "STREET1";
    private static final String STREET2 = "STREET2";
    private static final String CITY = "CITY";
    private static final String STATE = "ST";
    private static final String ZIP = "12345";
    private static final String PHONE = "(123) 456-7890";
    private static final String DISTRICT = "DISTRICT";
    private static final Boolean IS_PRIVITE = Boolean.TRUE;

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    void testCreateSchool() throws Exception {
        // setup the fixture
        InboundAddress inboundAddress = InboundAddress.builder()
                .withStreet1(STREET1)
                .withStreet2(STREET2)
                .withCity(CITY)
                .withState(STATE)
                .withZip(ZIP)
                .build();
        InboundSchool inboundSchool = InboundSchool.builder()
                .withName(NAME)
                .withAddress(inboundAddress)
                .withPhone(PHONE)
                .withDistrict(DISTRICT)
                .withIsPrivate(IS_PRIVITE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundSchool)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.address.street1", is(STREET1)))
                .andExpect(jsonPath("$.address.street2", is(STREET2)))
                .andExpect(jsonPath("$.address.city", is(CITY)))
                .andExpect(jsonPath("$.address.state", is(STATE)))
                .andExpect(jsonPath("$.address.zip", is(ZIP)))
                .andExpect(jsonPath("$.phone", is(PHONE)))
                .andExpect(jsonPath("$.district", is(DISTRICT)))
                .andExpect(jsonPath("$.isPrivate", is(IS_PRIVITE)))
                .andExpect(jsonPath("$._links.length()", is(3)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", endsWith("/teachers")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", endsWith("/programAdmins")));
    }

    @Test
    void testCreateSchool_fieldsInvalid() throws Exception {
        // setup the fixture
        InboundAddress inboundAddress = InboundAddress.builder()
                .withStreet1("123456789012345678901234567890123456789012345678901")
                .withStreet2("123456789012345678901234567890123456789012345678901")
                .withCity("123456789012345678901234567890123456789012345678901")
                .withState("123")
                .withZip("1234567890")
                .build();
        InboundSchool inboundSchool = InboundSchool.builder()
                .withName(
                        "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901")
                .withAddress(inboundAddress)
                .withPhone("12345678901")
                .withDistrict("123456789012345678901234567890123456789012345678901")
                .withIsPrivate(IS_PRIVITE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundSchool)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(8)))
                .andExpect(jsonPath("$.error.name", is("The school's name can not be longer than 100 characters")))
                .andExpect(jsonPath("$.error['address.street1']", is("An address's street1 can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error['address.street2']", is("An address's street2 can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error['address.city']", is("An address's city can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error['address.state']", is("An address's state must be 2 characters long")))
                .andExpect(jsonPath("$.error['address.zip']", is("An address's zip must be between 5 and 9 characters" +
                        " long")))
                .andExpect(jsonPath("$.error.phone", is("The school's phone number is invalid")))
                .andExpect(jsonPath("$.error.district", is("The school's district can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools")));
    }

    @Test
    void testCreateSchool_nullAddress() throws Exception {
        // setup the fixture
        InboundAddress inboundAddress = null;
        InboundSchool inboundSchool = InboundSchool.builder()
                .withName(NAME)
                .withAddress(inboundAddress)
                .withPhone(PHONE)
                .withDistrict(DISTRICT)
                .withIsPrivate(IS_PRIVITE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundSchool)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.address.street1", nullValue()))
                .andExpect(jsonPath("$.address.street2", nullValue()))
                .andExpect(jsonPath("$.address.city", nullValue()))
                .andExpect(jsonPath("$.address.state", nullValue()))
                .andExpect(jsonPath("$.address.zip", nullValue()))
                .andExpect(jsonPath("$.phone", is(PHONE)))
                .andExpect(jsonPath("$.district", is(DISTRICT)))
                .andExpect(jsonPath("$.isPrivate", is(IS_PRIVITE)))
                .andExpect(jsonPath("$._links.length()", is(3)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", endsWith("/teachers")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", endsWith("/programAdmins")));
    }

    @Test
    void testCreateSchool_nullAllowedFields() throws Exception {
        // setup the fixture
        InboundAddress inboundAddress = InboundAddress.builder()
                .withStreet1(null)
                .withStreet2(null)
                .withCity(null)
                .withState(null)
                .withZip(null)
                .build();
        InboundSchool inboundSchool = InboundSchool.builder()
                .withName(NAME)
                .withAddress(inboundAddress)
                .withPhone(null)
                .withDistrict(null)
                .withIsPrivate(null)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundSchool)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.address.street1", nullValue()))
                .andExpect(jsonPath("$.address.street2", nullValue()))
                .andExpect(jsonPath("$.address.city", nullValue()))
                .andExpect(jsonPath("$.address.state", nullValue()))
                .andExpect(jsonPath("$.address.zip", nullValue()))
                .andExpect(jsonPath("$.phone", nullValue()))
                .andExpect(jsonPath("$.district", nullValue()))
                .andExpect(jsonPath("$.isPrivate", nullValue()))
                .andExpect(jsonPath("$._links.length()", is(3)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", endsWith("/teachers")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", endsWith("/programAdmins")));
    }

    @Test
    void testCreateSchool_nullRequiredValues() throws Exception {
        // setup the fixture
        InboundAddress inboundAddress = InboundAddress.builder()
                .withStreet1(STREET1)
                .withStreet2(STREET2)
                .withCity(CITY)
                .withState(STATE)
                .withZip(ZIP)
                .build();
        InboundSchool inboundSchool = InboundSchool.builder()
                .withName(null)
                .withAddress(inboundAddress)
                .withPhone(PHONE)
                .withDistrict(DISTRICT)
                .withIsPrivate(IS_PRIVITE)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inboundSchool)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.name", is("A name is required for a school")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools")));
    }

    @Test
    void testDeactivate() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(delete("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isNoContent());
    }

    @Test
    void testGetAllSchools() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.schoolList.length()", is(2)))
                .andExpect(jsonPath("$._embedded.schoolList[0].id", is("e96a7158-da3f-48ef-b37a-f2833957bcbd")))
                .andExpect(jsonPath("$._embedded.schoolList[1].id", is("fd03c21f-cd39-4c05-b3f1-6d49618b6b10")));
    }

    @Test
    void testGetSchoolById_found() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("fd03c21f-cd39-4c05-b3f1-6d49618b6b10")))
                .andExpect(jsonPath("$.name", is("NAME2")))
                .andExpect(jsonPath("$.address.street1", is("STREET1")))
                .andExpect(jsonPath("$.address.street2", is("STREET2")))
                .andExpect(jsonPath("$.address.city", is("CITY")))
                .andExpect(jsonPath("$.address.state", is("ST")))
                .andExpect(jsonPath("$.address.zip", is("123456789")))
                .andExpect(jsonPath("$.phone", is("(360) 123-4567")))
                .andExpect(jsonPath("$.district", is("DISTRICT")))
                .andExpect(jsonPath("$.isPrivate", is(true)))
                .andExpect(jsonPath("$._links.length()", is(3)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", endsWith("/teachers")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", endsWith("/programAdmins")));
    }

    @Test
    void testGetSchoolById_notFound() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/ed03c21f-cd39-4c05-b3f1-6d49618b6b10")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isNotFound());
    }

    @Test
    void testUpdateSchool() throws Exception {
        // setup the fixture
        Map<String, Object> addressModel = new HashMap<>();
        addressModel.put("street1", "NEW STREET1");
        addressModel.put("street2", "NEW STREET2");
        addressModel.put("city", "NEW CITY");
        addressModel.put("state", "NW");
        addressModel.put("zip", "987654321");
        Map<String, Object> schoolModel = new HashMap<>();
        schoolModel.put("name", "NEW NAME");
        schoolModel.put("address", addressModel);
        schoolModel.put("phone", "(360) 765-4321");
        schoolModel.put("district", "NEW DISTRICT");
        schoolModel.put("isPrivate", false);

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(schoolModel)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.name", is("NEW NAME")))
                .andExpect(jsonPath("$.address.street1", is("NEW STREET1")))
                .andExpect(jsonPath("$.address.street2", is("NEW STREET2")))
                .andExpect(jsonPath("$.address.city", is("NEW CITY")))
                .andExpect(jsonPath("$.address.state", is("NW")))
                .andExpect(jsonPath("$.address.zip", is("987654321")))
                .andExpect(jsonPath("$.phone", is("(360) 765-4321")))
                .andExpect(jsonPath("$.district", is("NEW DISTRICT")))
                .andExpect(jsonPath("$.isPrivate", is(false)))
                .andExpect(jsonPath("$._links.length()", is(3)))
                .andExpect(jsonPath("$._links.self[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.teachers[0].href", endsWith("/teachers")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", startsWith("http://localhost/api/v1/schools/")))
                .andExpect(jsonPath("$._links.programAdmins[0].href", endsWith("/programAdmins")));
    }

}
