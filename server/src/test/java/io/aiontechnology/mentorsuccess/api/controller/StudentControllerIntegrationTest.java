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
import io.aiontechnology.mentorsuccess.model.enumeration.ContactMethod;
import io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundContact;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudent;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentMentor;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentTeacher;
import io.aiontechnology.mentorsuccess.security.SystemAdminAuthoritySetter;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
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
 * Tests for {@link StudentController}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({"/io/aiontechnology/mentorsuccess/api/controller/student-controller.sql"})
@Transactional
public class StudentControllerIntegrationTest {

    @Inject
    private MockMvc mvc;

    @Inject
    private ObjectMapper objectMapper;

    @Test
    void testCreateStudent() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String STUDENT_ID = "STUDENT_ID";
        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        String preferredTime = "PREFERRED";
        String actualTime = "ACTUAL";
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        int preBehavioralAssessment = 1;
        int postBehavioralAssessment = 5;
        InboundStudent studentModel = InboundStudent.builder()
                .withStudentId(STUDENT_ID)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withPreferredTime(preferredTime)
                .withActualTime(actualTime)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withPreBehavioralAssessment(preBehavioralAssessment)
                .withPostBehavioralAssessment(postBehavioralAssessment)
                .withTeacher(inboundStudentTeacher)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.studentId", is(STUDENT_ID)))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.grade", is(GRADE)))
                .andExpect(jsonPath("$.location", is(LOCATION.toString())))
                .andExpect(jsonPath("$.preferredTime", is(preferredTime)))
                .andExpect(jsonPath("$.actualTime", is(actualTime)))
                .andExpect(jsonPath("$.registrationSigned", is(IS_REGISTRATION_SIGNED)))
                .andExpect(jsonPath("$.mediaReleaseSigned", is(IS_MEDIA_RELEASE_SIGNED)))
                .andExpect(jsonPath("$.preBehavioralAssessment", is(preBehavioralAssessment)))
                .andExpect(jsonPath("$.postBehavioralAssessment", is(postBehavioralAssessment)));
    }

    @Test
    void testCreateStudentRequiredOnly() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.grade", is(GRADE)))
                .andExpect(jsonPath("$.location", is(LOCATION.toString())))
                .andExpect(jsonPath("$.registrationSigned", is(IS_REGISTRATION_SIGNED)))
                .andExpect(jsonPath("$.mediaReleaseSigned", is(IS_MEDIA_RELEASE_SIGNED)))
                .andExpect(jsonPath("$.teacher", notNullValue()))
                .andExpect(jsonPath("$.teacher.teacher.firstName", is("Fred")))
                .andExpect(jsonPath("$.teacher.teacher.lastName", is("Rogers")))
                .andExpect(jsonPath("$.teacher.teacher.email", is("fred@rogers.com")))
                .andExpect(jsonPath("$.teacher.teacher.workPhone", is("(360) 111-2222")))
                .andExpect(jsonPath("$.teacher.teacher.cellPhone", is("(360) 333-4444")))
                .andExpect(jsonPath("$.teacher.teacher.grade1", is(1)))
                .andExpect(jsonPath("$.teacher.teacher.grade2", is(2)))
                .andExpect(jsonPath("$.teacher.teacher._links.self[0].href", is(TEACHER_URI.toString())))
                .andExpect(jsonPath("$.teacher.comment", is(COMMENT)))
                .andExpect(jsonPath("$.mentor", nullValue()));
    }

    @Test
    void testCreateStudent_fieldsTooLong() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        final URI MENTOR_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/mentors/46771afb-a8ef-474e-b8e5" +
                        "-c693529cc5a8");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String STUDENT_ID = "123456789012345678901";
        String FIRST_NAME = "123456789012345678901234567890123456789012345678901";
        String LAST_NAME = "123456789012345678901234567890123456789012345678901";
        int GRADE = 1;
        String PREFERRED_TIME = "1234567890123456789012345678901";
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withStudentId(STUDENT_ID)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withPreferredTime(PREFERRED_TIME)
                .withLocation(LOCATION)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(4)))
                .andExpect(jsonPath("$.error.studentId", is("A student's id can not be longer than 20 characters")))
                .andExpect(jsonPath("$.error.firstName", is("A student's first name can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.lastName", is("A student's last name can not be longer than 50 " +
                        "characters")))
                .andExpect(jsonPath("$.error.preferredTime", is("A student's preferred time can not be longer than 30" +
                        " characters")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_invalidTeacherUri() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe04");
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.['Not found']", is("Unable to find specified teacher")))
                .andExpect(jsonPath("$.message", is("Not found")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_nullRequiredValues() throws Exception {
        // setup the fixture
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(null)
                .withLastName(null)
                .withGrade(null)
                .withLocation(null)
                .withRegistrationSigned(null)
                .withMediaReleaseSigned(null)
                .withTeacher(null)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(7)))
                .andExpect(jsonPath("$.error.firstName", is("A student must have a first name")))
                .andExpect(jsonPath("$.error.lastName", is("A student must have a last name")))
                .andExpect(jsonPath("$.error.grade", is("A student must have a grade")))
                .andExpect(jsonPath("$.error.location", is("A location is required for a student")))
                .andExpect(jsonPath("$.error.mediaReleaseSigned", is("A media release specification is required for a" +
                        " student")))
                .andExpect(jsonPath("$.error.registrationSigned", is("A parent signature specification is required " +
                        "for" +
                        " a student")))
                .andExpect(jsonPath("$.error.teacher", is("A student's teacher must be provided")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_nullTeacherValues() throws Exception {
        // setup the fixture
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(null)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.['teacher.uri']", is("A URI for a teacher is required")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_withBehaviors() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withBehaviors(new HashSet(Arrays.asList("Perfectionism", "Bullying / Tattling")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.behaviors.length()", is(2)))
                .andExpect(jsonPath("$.behaviors", hasItem("Perfectionism")))
                .andExpect(jsonPath("$.behaviors", hasItem("Bullying / Tattling")));
    }

    @Test
    void testCreateStudent_withContacts() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String LABEL = "Parent";
        String CONTACT_FIRST_NAME = "CONTACT_FIRST_NAME";
        String CONTACT_LAST_NAME = "CONTACT_LAST_NAME";
        Boolean IS_EMERGENCY_CONTACT = false;
        String phone = "(123) 456-7890";
        ContactMethod CONTACT_METHOD = ContactMethod.PHONE;
        InboundContact inboundContact = InboundContact.builder()
                .withLabel(LABEL)
                .withFirstName(CONTACT_FIRST_NAME)
                .withLastName(CONTACT_LAST_NAME)
                .withIsEmergencyContact(IS_EMERGENCY_CONTACT)
                .withPhone(phone)
                .withPreferredContactMethod(CONTACT_METHOD)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withContacts(new HashSet(Arrays.asList(inboundContact)))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.contacts[0].label", is(LABEL)))
                .andExpect(jsonPath("$.contacts[0].firstName", is(CONTACT_FIRST_NAME)))
                .andExpect(jsonPath("$.contacts[0].lastName", is(CONTACT_LAST_NAME)))
                .andExpect(jsonPath("$.contacts[0].isEmergencyContact", is(IS_EMERGENCY_CONTACT)))
                .andExpect(jsonPath("$.contacts[0].phone", is(phone)))
                .andExpect(jsonPath("$.contacts[0].preferredContactMethod", is(CONTACT_METHOD.toString())));
    }

    @Test
    void testCreateStudent_withInterests() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withInterests(new HashSet(Arrays.asList("Cats", "Dogs")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.interests.length()", is(2)))
                .andExpect(jsonPath("$.interests", hasItem("Cats")))
                .andExpect(jsonPath("$.interests", hasItem("Dogs")));
    }

    @Test
    void testCreateStudent_withInvalidBehavior() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withBehaviors(new HashSet<String>(Arrays.asList("INVALID")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.['Not found']", is("Invalid behavior: INVALID")))
                .andExpect(jsonPath("$.message", is("Not found")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_withInvalidContact() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        InboundContact inboundContact = InboundContact.builder()
                .withFirstName(null)
                .withLastName(null)
                .withIsEmergencyContact(null)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withContacts(new HashSet(Arrays.asList(inboundContact)))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(4)))
                .andExpect(jsonPath("$.error.['contacts[0]']", is("A contact must have at least one contact method.")))
                .andExpect(jsonPath("$.error.['contacts[0].firstName']", is("A contact must have a first name")))
                .andExpect(jsonPath("$.error.['contacts[0].lastName']", is("A contact must have a last name")))
                .andExpect(jsonPath("$.error.['contacts[0].isEmergencyContact']", is("The contact must be specified " +
                        "as an emergency contact or not")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    @Disabled("Currently the API will ignore invalid interests")
    void testCreateStudent_withInvalidInterest() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withInterests(new HashSet(Arrays.asList("INVALID")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.['Not found']", is("Invalid interest: INVALID")))
                .andExpect(jsonPath("$.message", is("Not found")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_withInvalidLeadershipSkill() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withLeadershipSkills(new HashSet(Arrays.asList("INVALID")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.['Not found']", is("Invalid leadership skill: INVALID")))
                .andExpect(jsonPath("$.message", is("Not found")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_withInvalidLeadershipTrait() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withLeadershipTraits(new HashSet(Arrays.asList("INVALID")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.['Not found']", is("Invalid leadership trait: INVALID")))
                .andExpect(jsonPath("$.message", is("Not found")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_withInvalidMentor() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        final URI MENTOR_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/mentors/46771afb-a8ef-474e-b8e5" +
                        "-c693529cc5a9");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();
        InboundStudentMentor inboundStudentMentor = InboundStudentMentor.builder()
                .withUri(MENTOR_URI)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withMentor(inboundStudentMentor)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error.length()", is(1)))
                .andExpect(jsonPath("$.error.['Not found']", is("Unable to find specified mentor")))
                .andExpect(jsonPath("$.message", is("Not found")))
                .andExpect(jsonPath("$.path", is("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")));
    }

    @Test
    void testCreateStudent_withLeadershipSkills() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withLeadershipSkills(new HashSet(Arrays.asList("Decision Making", "Planning")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.leadershipSkills.length()", is(2)))
                .andExpect(jsonPath("$.leadershipSkills", hasItem("Decision Making")))
                .andExpect(jsonPath("$.leadershipSkills", hasItem("Planning")));
    }

    @Test
    void testCreateStudent_withLeadershipTraits() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date();
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withLeadershipTraits(new HashSet(Arrays.asList("Humility", "Responsibility")))
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.leadershipTraits.length()", is(2)))
                .andExpect(jsonPath("$.leadershipTraits", hasItem("Humility")))
                .andExpect(jsonPath("$.leadershipTraits", hasItem("Responsibility")));
    }

    @Test
    void testCreateStudent_withMentor() throws Exception {
        // setup the fixture
        final URI TEACHER_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers/ba238442-ce51-450d" +
                        "-a474-2e36872abe05");
        final URI MENTOR_URI = URI.create(
                "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/mentors/46771afb-a8ef-474e-b8e5" +
                        "-c693529cc5a8");
        String COMMENT = "COMMENT";
        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder()
                .withUri(TEACHER_URI)
                .withComment(COMMENT)
                .build();
        InboundStudentMentor inboundStudentMentor = InboundStudentMentor.builder()
                .withUri(MENTOR_URI)
                .build();

        String FIRST_NAME = "FIRST_NAME";
        String LAST_NAME = "LAST_NAME";
        int GRADE = 1;
        ResourceLocation LOCATION = ResourceLocation.OFFLINE;
        Date startDate = new Date(121, 2, 9);
        Boolean IS_REGISTRATION_SIGNED = true;
        Boolean IS_MEDIA_RELEASE_SIGNED = true;
        InboundStudent studentModel = InboundStudent.builder()
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withGrade(GRADE)
                .withLocation(LOCATION)
                .withStartDate(startDate)
                .withRegistrationSigned(IS_REGISTRATION_SIGNED)
                .withMediaReleaseSigned(IS_MEDIA_RELEASE_SIGNED)
                .withTeacher(inboundStudentTeacher)
                .withMentor(inboundStudentMentor)
                .build();

        // execute the SUT
        ResultActions result = mvc.perform(post("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.grade", is(GRADE)))
                .andExpect(jsonPath("$.location", is(LOCATION.toString())))
                .andExpect(jsonPath("$.startDate", is(new SimpleDateFormat("yyyy-MM-dd").format(startDate))))
                .andExpect(jsonPath("$.registrationSigned", is(IS_REGISTRATION_SIGNED)))
                .andExpect(jsonPath("$.mediaReleaseSigned", is(IS_MEDIA_RELEASE_SIGNED)))
                .andExpect(jsonPath("$.teacher", notNullValue()))
                .andExpect(jsonPath("$.teacher.teacher.firstName", is("Fred")))
                .andExpect(jsonPath("$.teacher.teacher.lastName", is("Rogers")))
                .andExpect(jsonPath("$.teacher.teacher.email", is("fred@rogers.com")))
                .andExpect(jsonPath("$.teacher.teacher.workPhone", is("(360) 111-2222")))
                .andExpect(jsonPath("$.teacher.teacher.cellPhone", is("(360) 333-4444")))
                .andExpect(jsonPath("$.teacher.teacher.grade1", is(1)))
                .andExpect(jsonPath("$.teacher.teacher.grade2", is(2)))
                .andExpect(jsonPath("$.teacher.teacher._links.self[0].href", is(TEACHER_URI.toString())))
                .andExpect(jsonPath("$.teacher.comment", is(COMMENT)))
                .andExpect(jsonPath("$.mentor", notNullValue()))
                .andExpect(jsonPath("$.mentor.mentor.firstName", is("Mark")))
                .andExpect(jsonPath("$.mentor.mentor.lastName", is("Mentor")))
                .andExpect(jsonPath("$.mentor.mentor.email", is("mark@mentor.com")))
                .andExpect(jsonPath("$.mentor.mentor.workPhone", is("(360) 222-3333")))
                .andExpect(jsonPath("$.mentor.mentor.cellPhone", is("(360) 444-5555")))
                .andExpect(jsonPath("$.mentor.mentor._links.self[0].href", is(MENTOR_URI.toString())));
    }

    @Test
    void testDeactivateStudent() throws Exception {
        // setup the fixture
        // See SQL file

        // execute the SUT
        ResultActions result = mvc.perform(delete("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students" +
                "/2a8c5871-a21d-47a1-a516-a6376a6b8bf2")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build())));

        // validation
        result.andExpect(status().isNoContent());
    }

    @Test
    void testReadStudent() throws Exception {
        // setup the fixture

        // execute the SUT
        ResultActions result = mvc.perform(get("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students" +
                "/2a8c5871-a21d-47a1-a516-a6376a6b8bf2")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is("2a8c5871-a21d-47a1-a516-a6376a6b8bf2")))
                .andExpect(jsonPath("$.firstName", is("Sam")))
                .andExpect(jsonPath("$.lastName", is("Student")))
                .andExpect(jsonPath("$.grade", is(2)))
                .andExpect(jsonPath("$.preferredTime", is("2:00pm")))
                .andExpect(jsonPath("$.location", is("OFFLINE")))
                .andExpect(jsonPath("$.preBehavioralAssessment", is(1)))
                .andExpect(jsonPath("$.postBehavioralAssessment", is(5)))
                .andExpect(jsonPath("$.mediaReleaseSigned", is(true)));
    }

    @Test
    void testUpdateStudent() throws Exception {
        // setup the fixture
        Map<String, Object> teacherModel = new HashMap<>();
        teacherModel.put("uri", "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers" +
                "/ba238442-ce51-450d-a474-2e36872abe05");
        teacherModel.put("comment", "We need to talk");

        Map<String, Object> mentorModel = new HashMap<>();
        mentorModel.put("uri", "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/mentors/46771afb" +
                "-a8ef-474e-b8e5-c693529cc5a8");
        mentorModel.put("time", "Whenever");
        mentorModel.put("location", "ONLINE");
        mentorModel.put("mediaReleaseSigned", true);

        Set<String> behaviors = Set.of("Perfectionism", "Bullying / Tattling");
        Set<String> interests = Set.of("Cats", "Dogs");
        Set<String> leadershipSkills = Set.of("Decision Making", "Planning");
        Set<String> leadershipTraits = Set.of("Humility", "Responsibility");

        Map<String, Object> contact1 = new HashMap<>();
        contact1.put("label", "Parent");
        contact1.put("firstName", "Peter");
        contact1.put("lastName", "Parent");
        contact1.put("isEmergencyContact", true);
        contact1.put("phone", "(123) 456-7890");

        Map<String, Object> studentModel = new HashMap<>();
        studentModel.put("firstName", "NEW FIRST NAME");
        studentModel.put("lastName", "NEW LAST NAME");
        studentModel.put("grade", 3);
        studentModel.put("preferredTime", "10:00am");
        studentModel.put("startDate", "2020-12-01");
        studentModel.put("location", "OFFLINE");
        studentModel.put("registrationSigned", false);
        studentModel.put("mediaReleaseSigned", false);
        studentModel.put("preBehavioralAssessment", 2);
        studentModel.put("postBehavioralAssessment", 6);
        studentModel.put("teacher", teacherModel);
        studentModel.put("mentor", mentorModel);
        studentModel.put("behaviors", behaviors);
        studentModel.put("interests", interests);
        studentModel.put("leadershipSkills", leadershipSkills);
        studentModel.put("leadershipTraits", leadershipTraits);
        studentModel.put("contacts", Arrays.asList(contact1));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students" +
                "/2a8c5871-a21d-47a1-a516-a6376a6b8bf2")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is("NEW FIRST NAME")))
                .andExpect(jsonPath("$.lastName", is("NEW LAST NAME")))
                .andExpect(jsonPath("$.grade", is(3)))
                .andExpect(jsonPath("$.startDate", is("2020-12-01")))
                .andExpect(jsonPath("$.preferredTime", is("10:00am")))
                .andExpect(jsonPath("$.location", is("OFFLINE")))
                .andExpect(jsonPath("$.registrationSigned", is(false)))
                .andExpect(jsonPath("$.mediaReleaseSigned", is(false)))
                .andExpect(jsonPath("$.preBehavioralAssessment", is(2)))
                .andExpect(jsonPath("$.postBehavioralAssessment", is(6)))
                .andExpect(jsonPath("$.teacher.teacher.firstName", is("Fred")))
                .andExpect(jsonPath("$.teacher.teacher.lastName", is("Rogers")))
                .andExpect(jsonPath("$.mentor.mentor.firstName", is("Mark")))
                .andExpect(jsonPath("$.mentor.mentor.lastName", is("Mentor")))
                .andExpect(jsonPath("$.behaviors.size()", is(2)))
                .andExpect(jsonPath("$.behaviors", hasItems("Perfectionism", "Bullying / Tattling")))
                .andExpect(jsonPath("$.interests", hasItems("Cats", "Dogs")))
                .andExpect(jsonPath("$.leadershipSkills", hasItems("Decision Making", "Planning")))
                .andExpect(jsonPath("$.leadershipTraits", hasItems("Humility", "Responsibility")))
                .andExpect(jsonPath("$.contacts[0].label", is("Parent")))
                .andExpect(jsonPath("$.contacts[0].firstName", is("Peter")))
                .andExpect(jsonPath("$.contacts[0].lastName", is("Parent")))
                .andExpect(jsonPath("$.contacts[0].isEmergencyContact", is(true)));
    }

    @Test
    void testUpdateStudent_fromNullMentor() throws Exception {
        // setup the fixture
        Map<String, Object> teacherModel = new HashMap<>();
        teacherModel.put("uri", "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers" +
                "/ba238442-ce51-450d-a474-2e36872abe05");
        teacherModel.put("comment", "We need to talk");

        Set<String> behaviors = Set.of("Perfectionism", "Bullying / Tattling");
        Set<String> interests = Set.of("Cats", "Dogs");
        Set<String> leadershipSkills = Set.of("Decision Making", "Planning");
        Set<String> leadershipTraits = Set.of("Humility", "Responsibility");

        Map<String, Object> contact1 = new HashMap<>();
        contact1.put("label", "Parent");
        contact1.put("firstName", "Peter");
        contact1.put("lastName", "Parent");
        contact1.put("isEmergencyContact", true);
        contact1.put("phone", "(123) 456-7890");

        Map<String, Object> studentModel = new HashMap<>();
        studentModel.put("firstName", "NEW FIRST NAME");
        studentModel.put("lastName", "NEW LAST NAME");
        studentModel.put("grade", 3);
        studentModel.put("preferredTime", "10:00am");
        studentModel.put("startDate", "2020-12-01");
        studentModel.put("location", "OFFLINE");
        studentModel.put("registrationSigned", false);
        studentModel.put("mediaReleaseSigned", false);
        studentModel.put("teacher", teacherModel);
        studentModel.put("mentor", null);
        studentModel.put("behaviors", behaviors);
        studentModel.put("interests", interests);
        studentModel.put("leadershipSkills", leadershipSkills);
        studentModel.put("leadershipTraits", leadershipTraits);
        studentModel.put("contacts", Arrays.asList(contact1));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students" +
                "/f0c08c26-954b-4d05-8536-522403f9e54e")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is("NEW FIRST NAME")))
                .andExpect(jsonPath("$.lastName", is("NEW LAST NAME")))
                .andExpect(jsonPath("$.grade", is(3)))
                .andExpect(jsonPath("$.preferredTime", is("10:00am")))
                .andExpect(jsonPath("$.startDate", is("2020-12-01")))
                .andExpect(jsonPath("$.location", is("OFFLINE")))
                .andExpect(jsonPath("$.registrationSigned", is(false)))
                .andExpect(jsonPath("$.mediaReleaseSigned", is(false)))
                .andExpect(jsonPath("$.behaviors.size()", is(2)))
                .andExpect(jsonPath("$.behaviors", hasItems("Perfectionism", "Bullying / Tattling")))
                .andExpect(jsonPath("$.interests", hasItems("Cats", "Dogs")))
                .andExpect(jsonPath("$.leadershipSkills", hasItems("Decision Making", "Planning")))
                .andExpect(jsonPath("$.leadershipTraits", hasItems("Humility", "Responsibility")))
                .andExpect(jsonPath("$.contacts[0].label", is("Parent")))
                .andExpect(jsonPath("$.contacts[0].firstName", is("Peter")))
                .andExpect(jsonPath("$.contacts[0].lastName", is("Parent")))
                .andExpect(jsonPath("$.contacts[0].isEmergencyContact", is(true)));
    }

    @Test
    void testUpdateStudent_toNullMentor() throws Exception {
        // setup the fixture
        Map<String, Object> teacherModel = new HashMap<>();
        teacherModel.put("uri", "http://localhost/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/teachers" +
                "/ba238442-ce51-450d-a474-2e36872abe05");
        teacherModel.put("comment", "We need to talk");

        Set<String> behaviors = Set.of("Perfectionism", "Bullying / Tattling");
        Set<String> interests = Set.of("Cats", "Dogs");
        Set<String> leadershipSkills = Set.of("Decision Making", "Planning");
        Set<String> leadershipTraits = Set.of("Humility", "Responsibility");

        Map<String, Object> contact1 = new HashMap<>();
        contact1.put("label", "Parent");
        contact1.put("firstName", "Peter");
        contact1.put("lastName", "Parent");
        contact1.put("isEmergencyContact", true);
        contact1.put("phone", "(123) 456-7890");

        Map<String, Object> studentModel = new HashMap<>();
        studentModel.put("firstName", "NEW FIRST NAME");
        studentModel.put("lastName", "NEW LAST NAME");
        studentModel.put("grade", 3);
        studentModel.put("preferredTime", "10:00am");
        studentModel.put("location", "OFFLINE");
        studentModel.put("registrationSigned", false);
        studentModel.put("mediaReleaseSigned", false);
        studentModel.put("teacher", teacherModel);
        studentModel.put("mentor", null);
        studentModel.put("behaviors", behaviors);
        studentModel.put("interests", interests);
        studentModel.put("leadershipSkills", leadershipSkills);
        studentModel.put("leadershipTraits", leadershipTraits);
        studentModel.put("contacts", Arrays.asList(contact1));

        // execute the SUT
        ResultActions result = mvc.perform(put("/api/v1/schools/fd03c21f-cd39-4c05-b3f1-6d49618b6b10/students" +
                "/2a8c5871-a21d-47a1-a516-a6376a6b8bf2")
                .with(jwt().jwt(Jwt.withTokenValue("1234")
                        .claim("cognito:groups", new SystemAdminAuthoritySetter())
                        .header("test", "value")
                        .build()))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentModel)));

        // validation
        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.firstName", is("NEW FIRST NAME")))
                .andExpect(jsonPath("$.lastName", is("NEW LAST NAME")))
                .andExpect(jsonPath("$.grade", is(3)))
                .andExpect(jsonPath("$.preferredTime", is("10:00am")))
                .andExpect(jsonPath("$.location", is("OFFLINE")))
                .andExpect(jsonPath("$.registrationSigned", is(false)))
                .andExpect(jsonPath("$.mediaReleaseSigned", is(false)))
                .andExpect(jsonPath("$.teacher.teacher.firstName", is("Fred")))
                .andExpect(jsonPath("$.teacher.teacher.lastName", is("Rogers")))
                .andExpect(jsonPath("$.behaviors.size()", is(2)))
                .andExpect(jsonPath("$.behaviors", hasItems("Perfectionism", "Bullying / Tattling")))
                .andExpect(jsonPath("$.interests", hasItems("Cats", "Dogs")))
                .andExpect(jsonPath("$.leadershipSkills", hasItems("Decision Making", "Planning")))
                .andExpect(jsonPath("$.leadershipTraits", hasItems("Humility", "Responsibility")))
                .andExpect(jsonPath("$.contacts[0].label", is("Parent")))
                .andExpect(jsonPath("$.contacts[0].firstName", is("Peter")))
                .andExpect(jsonPath("$.contacts[0].lastName", is("Parent")))
                .andExpect(jsonPath("$.contacts[0].isEmergencyContact", is(true)));
    }

}
