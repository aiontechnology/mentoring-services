/*
 * Copyright 2020-2022 Aion Technology LLC
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
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentPersonRole;
import io.aiontechnology.mentoring.model.inbound.student.InboundContact;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Map an {@link InboundStudent} instance to a {@link Student} entity.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class StudentModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundStudent, Student> {

    // Mappers
    private final OneWayCollectionUpdateMapper<InboundContact, StudentPersonRole> contactsMapper;

    /**
     * Map the given {@link InboundStudent} to the given {@link Student}.
     *
     * @param inboundStudent The {@link InboundStudent} to map from.
     * @param student The {@link Student} to map to.
     * @return The mapped {@link Student}.
     */
    @Override
    public Optional<Student> map(InboundStudent inboundStudent, Student student) {
        requireNonNull(inboundStudent);
        return Optional.ofNullable(student)
                .map(s -> mapProperties(inboundStudent, s))
                .map(s -> mapContacts(inboundStudent, s));
    }

    private Student mapContacts(InboundStudent inboundStudent, Student student) {
        Collection<StudentPersonRole> roles = Optional.ofNullable(contactsMapper)
                .map(mapper -> mapper.map(inboundStudent.getContacts(), student.getStudentPersonRoles()))
                .orElse(Collections.emptyList());
        roles.stream()
                .forEach(r -> r.setStudent(student));
        return student;
    }

    private Student mapProperties(InboundStudent inboundStudent, Student student) {
        student.setStudentId(inboundStudent.getStudentId());
        student.setFirstName(inboundStudent.getFirstName());
        student.setLastName(inboundStudent.getLastName());
        return student;
    }

}
