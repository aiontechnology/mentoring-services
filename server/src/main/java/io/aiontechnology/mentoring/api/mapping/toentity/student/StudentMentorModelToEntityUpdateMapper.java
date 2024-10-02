/*
 * Copyright 2020-2021 Aion Technology LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.StudentMentor;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentMentor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 0.6.0
 */
@Component
@RequiredArgsConstructor
public class StudentMentorModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundStudentMentor, StudentMentor> {

    private final OneWayMapper<URI, SchoolPersonRole> mentorModelToEntityMapper;

    @Override
    public Optional<StudentMentor> map(InboundStudentMentor inboundStudentMentor, StudentMentor studentMentor) {
        return Optional.ofNullable(inboundStudentMentor)
                .map(studentMentorModel -> {
                    Optional<SchoolPersonRole> requestedRole =
                            mentorModelToEntityMapper.map(studentMentorModel.getUri());

                    boolean matches = requestedRole
                            .map(role -> role.equals(studentMentor.getRole()))
                            .orElse(false);

                    StudentMentor newStudentMentor = matches ? studentMentor : new StudentMentor();
                    newStudentMentor.setRole(requestedRole
                            .orElseThrow(() -> new NotFoundException("Unable to find specified mentor")));
                    return newStudentMentor;
                });
    }

}
