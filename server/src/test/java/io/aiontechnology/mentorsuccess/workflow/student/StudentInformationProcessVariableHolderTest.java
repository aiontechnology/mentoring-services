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
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.entity.Student;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudentInformationProcessVariableHolderTest {

    @Test
    void testRequiredOnly() {
        // set up the fixture
        var schoolId = UUID.randomUUID();
        var studentId = UUID.randomUUID();
        var schoolEmailTag = "SCHOOL_EMAIL_TAG";
        var registrationBase = "REGISTRATION_BASE";

        var school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        when(school.getRequiredIdString(anyString())).thenCallRealMethod();
        when(school.getEmailTag()).thenReturn(schoolEmailTag);
        var student = mock(Student.class);
        when(student.getId()).thenReturn(studentId);
        when(student.getRequiredIdString(anyString())).thenCallRealMethod();

        // execute the SUT
        var result = StudentInformationProcessVariableHolder.builder(school, student, registrationBase)
                .build()
                .processVariables();
        // validation
        assertThat(result.size()).isEqualTo(5);
        assertThat(result).contains(
                Map.entry("schoolId", schoolId.toString()),
                Map.entry("studentId", studentId.toString()),
                Map.entry("schoolEmailTag", schoolEmailTag),
                Map.entry("registrationBase", registrationBase),
                Map.entry("registrationTimeout", "P7D")
        );
    }

    @Test
    void testNullEmailTag() {
        // set up the fixture
        var schoolId = UUID.randomUUID();
        var studentId = UUID.randomUUID();
        String schoolEmailTag = null;
        var registrationBase = "REGISTRATION_BASE";

        var school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        when(school.getRequiredIdString(anyString())).thenCallRealMethod();
        when(school.getEmailTag()).thenReturn(schoolEmailTag);
        var student = mock(Student.class);
        when(student.getId()).thenReturn(studentId);
        when(student.getRequiredIdString(anyString())).thenCallRealMethod();

        // execute the SUT
        var result = StudentInformationProcessVariableHolder.builder(school, student, registrationBase)
                .build()
                .processVariables();
        // validation
        assertThat(result.size()).isEqualTo(4);
        assertThat(result).contains(
                Map.entry("schoolId", schoolId.toString()),
                Map.entry("studentId", studentId.toString()),
                Map.entry("registrationBase", registrationBase),
                Map.entry("registrationTimeout", "P7D")
        );
    }

    @Test
    void testWithTeacher() {
        // set up the fixture
        var schoolId = UUID.randomUUID();
        var studentId = UUID.randomUUID();
        var teacherId = UUID.randomUUID();
        var schoolEmailTag = "SCHOOL_EMAIL_TAG";
        var registrationBase = "REGISTRATION_BASE";

        var school = mock(School.class);
        when(school.getId()).thenReturn(schoolId);
        when(school.getRequiredIdString(anyString())).thenCallRealMethod();
        when(school.getEmailTag()).thenReturn(schoolEmailTag);
        var student = mock(Student.class);
        when(student.getId()).thenReturn(studentId);
        when(student.getRequiredIdString(anyString())).thenCallRealMethod();
        var teacher = mock(SchoolPersonRole.class);
        when(teacher.getId()).thenReturn(teacherId);
        when(teacher.getRequiredIdString(anyString())).thenCallRealMethod();

        // execute the SUT
        var result = StudentInformationProcessVariableHolder.builder(school, student, registrationBase)
                .withTeacher(teacher)
                .build()
                .processVariables();

        // validation
        assertThat(result.size()).isEqualTo(6);
        assertThat(result).contains(
                Map.entry("schoolId", schoolId.toString()),
                Map.entry("studentId", studentId.toString()),
                Map.entry("teacherId", teacherId.toString()),
                Map.entry("schoolEmailTag", schoolEmailTag),
                Map.entry("registrationBase", registrationBase),
                Map.entry("registrationTimeout", "P7D")
        );
    }

}
