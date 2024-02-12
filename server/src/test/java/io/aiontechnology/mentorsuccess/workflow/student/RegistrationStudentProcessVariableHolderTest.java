/*
 * Copyright 2024 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.workflow.student;

import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistrationStudentProcessVariableHolderTest {

    @Test
    void testRequiredOnly() {
        // set up the fixture
        var schoolId = UUID.randomUUID();
        var emailTag = "EMAIL_TAG";

        var school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        when(school.getRequiredIdString(anyString())).thenCallRealMethod();
        when(school.getEmailTag()).thenReturn(emailTag);
        var inboundInvitation = InboundInvitation.builder().build();

        // execute the SUT
        var result = StudentRegistrationProcessVariableHolder.builder(school, inboundInvitation)
                .build()
                .processVariables();

        // validation
        assertThat(result.size()).isEqualTo(4);
        assertThat(result).contains(
                Map.entry("schoolId", schoolId.toString()),
                Map.entry("invitation", inboundInvitation),
                Map.entry("schoolEmailTag", emailTag),
                Map.entry("registrationTimeout", "P7D")
        );
    }

    @Test
    void testNullEmailTag() {
        // set up the fixture
        var schoolId = UUID.randomUUID();
        String emailTag = null;

        var school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        when(school.getRequiredIdString(anyString())).thenCallRealMethod();
        when(school.getEmailTag()).thenReturn(emailTag);
        var inboundInvitation = InboundInvitation.builder().build();

        // execute the SUT
        var result = StudentRegistrationProcessVariableHolder.builder(school, inboundInvitation)
                .build()
                .processVariables();

        // validation
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(
                Map.entry("schoolId", schoolId.toString()),
                Map.entry("invitation", inboundInvitation),
                Map.entry("registrationTimeout", "P7D")
        );
    }

    @Test
    void testNullInvitation() {
        // set up the fixture
        var inboundInvitation = InboundInvitation.builder().build();

        // execute the SUT
        var result = assertThrows(NullPointerException.class, () ->
                StudentRegistrationProcessVariableHolder.builder(null, inboundInvitation)
                        .build()
                        .processVariables());

        // validation
        assertThat(result.getMessage()).isEqualTo("A school is required");
    }

    @Test
    void testNullSchool() {
        // set up the fixture
        var school = mock(School.class);

        // execute the SUT
        var result = assertThrows(NullPointerException.class, () ->
                StudentRegistrationProcessVariableHolder.builder(school, null)
                        .build()
                        .processVariables());

        // validation
        assertThat(result.getMessage()).isEqualTo("An invitation is required");
    }

    @Test
    void testNullSchoolId() {
        // set up the fixture
        var school = mock(School.class);
        when(school.getId()).thenReturn(null);
        when(school.getRequiredIdString(anyString())).thenCallRealMethod();
        var inboundInvitation = InboundInvitation.builder().build();

        // execute the SUT
        var result = assertThrows(NullPointerException.class, () ->
                StudentRegistrationProcessVariableHolder.builder(school, inboundInvitation)
                        .build()
                        .processVariables());

        // validation
        assertThat(result.getMessage()).isEqualTo("A school must have an ID");
    }

    @Test
    void testWithTimeout() {
        // set up the fixture
        var schoolId = UUID.randomUUID();
        var emailTag = "EMAIL_TAG";
        var timeout = "TIMEOUT";

        var school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        when(school.getRequiredIdString(anyString())).thenCallRealMethod();
        when(school.getEmailTag()).thenReturn(emailTag);
        var inboundInvitation = InboundInvitation.builder().build();

        // execute the SUT
        var result = StudentRegistrationProcessVariableHolder.builder(school, inboundInvitation)
                .withRegistrationTimeout(timeout)
                .build()
                .processVariables();

        // validation
        assertThat(result.size()).isEqualTo(4);
        assertThat(result).contains(
                Map.entry("schoolId", schoolId.toString()),
                Map.entry("invitation", inboundInvitation),
                Map.entry("schoolEmailTag", emailTag),
                Map.entry("registrationTimeout", timeout)
        );
    }

    @Test
    void testWithNullTimeout() {
        // set up the fixture
        String timeout = null;

        var school = mock(School.class);
        var inboundInvitation = InboundInvitation.builder().build();

        // execute the SUT
        var result = assertThrows(NullPointerException.class, () ->
                StudentRegistrationProcessVariableHolder.builder(school, inboundInvitation)
                        .withRegistrationTimeout(timeout)
                        .build()
                        .processVariables());

        // validation
        assertThat(result.getMessage()).isEqualTo("A registration timeout is required");
    }

}
