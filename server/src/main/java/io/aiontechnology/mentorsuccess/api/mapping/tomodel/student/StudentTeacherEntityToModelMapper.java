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

package io.aiontechnology.mentorsuccess.api.mapping.tomodel.student;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.entity.StudentTeacher;
import io.aiontechnology.mentorsuccess.model.outbound.OutboundTeacher;
import io.aiontechnology.mentorsuccess.model.outbound.student.OutboundStudentTeacher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class StudentTeacherEntityToModelMapper implements OneWayMapper<StudentTeacher, OutboundStudentTeacher> {

    @Qualifier("teacherAssemblerMapperAdaptor")
    private final OneWayMapper<SchoolPersonRole, OutboundTeacher> teacherEntityToModelMapper;

    @Override
    public Optional<OutboundStudentTeacher> map(StudentTeacher studentTeacher) {
        return Optional.ofNullable(studentTeacher)
                .map(s -> OutboundStudentTeacher.builder()
                        .withTeacher(teacherEntityToModelMapper.map(s.getRole()).orElse(null))
                        .withComment(s.getComment())
                        .build());
    }

}