/*
 * Copyright 2022-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayCollectionUpdateMapper;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentPersonRole;
import io.aiontechnology.mentoring.model.inbound.student.InboundContact;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class StudentModelToEntityUpdateMapperTest {

    @Test
    void testMapContacts() {
        // set up the fixture
        Collection<InboundContact> inboundContacts = new ArrayList<>();
        InboundStudent inboundStudent = InboundStudent.builder()
                .withContacts(inboundContacts)
                .build();

        OneWayCollectionUpdateMapper<InboundContact, StudentPersonRole> contactMapper =
                mock(OneWayCollectionUpdateMapper.class);

        StudentModelToEntityUpdateMapper mapper = new StudentModelToEntityUpdateMapper(contactMapper);

        Collection<StudentPersonRole> studentPersonRoles = new ArrayList<>();
        Student student = new Student();
        student.setStudentPersonRoles(studentPersonRoles);

        // execute the SUT
        mapper.map(inboundStudent, student);

        // validation
        verify(contactMapper).map(inboundContacts, studentPersonRoles);
    }

    @Test
    void testMapProperties() {
        // set up the fixture
        String studentId = "STUDENT_ID";
        String firstName = "FIRST";
        String lastName = "LAST";

        InboundStudent inboundStudent = InboundStudent.builder()
                .withStudentId(studentId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        StudentModelToEntityUpdateMapper mapper = new StudentModelToEntityUpdateMapper(null);

        Student student = new Student();

        // execute the SUT
        Optional<Student> result = mapper.map(inboundStudent, student);

        // validation
        assertThat(result.isPresent());
        Student studentResult = result.get();
        assertThat(studentResult.getStudentId()).isEqualTo(studentId);
        assertThat(studentResult.getFirstName()).isEqualTo(firstName);
        assertThat(studentResult.getLastName()).isEqualTo(lastName);
    }

}
