/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentorsuccess.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.entity.StudentTeacher;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentTeacher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class StudentTeacherModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundStudentTeacher, StudentTeacher> {

    private final OneWayMapper<URI, SchoolPersonRole> teacherModelToEntityMapper;

    @Override
    public Optional<StudentTeacher> map(InboundStudentTeacher inboundStudentTeacher, StudentTeacher studentTeacher) {
        return Optional.ofNullable(inboundStudentTeacher)
                .map(studentTeacherModel -> {
                    Optional<SchoolPersonRole> requestedRole =
                            teacherModelToEntityMapper.map(studentTeacherModel.getUri());

                    boolean matches = requestedRole
                            .map(role -> role.equals(studentTeacher.getRole()))
                            .orElse(false);

                    StudentTeacher newStudentTeacher = matches ? studentTeacher : new StudentTeacher();
                    newStudentTeacher.setRole(requestedRole
                            .orElseThrow(() -> new NotFoundException("Unable to find specified teacher")));
                    newStudentTeacher.setComment(studentTeacherModel.getComment());
                    return newStudentTeacher;
                });
    }

}
