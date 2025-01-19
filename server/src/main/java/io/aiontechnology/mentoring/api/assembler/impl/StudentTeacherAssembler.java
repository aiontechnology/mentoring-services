/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.assembler.impl;

import io.aiontechnology.mentoring.api.assembler.AssemblerSupport;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.model.outbound.OutboundTeacher;
import io.aiontechnology.mentoring.resource.StudentTeacherResource;

import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class StudentTeacherAssembler extends AssemblerSupport<StudentSchoolSession, StudentTeacherResource> {

    @Override
    protected Optional<StudentTeacherResource> doMap(StudentSchoolSession studentSchoolSession) {
        return Optional.ofNullable(studentSchoolSession)
                .map(session -> {

                    Object teacher = getSubMapper("teacher")
                            .map(session.getTeacher())
                            .orElse(null);
                    if(teacher != null) {
                        StudentTeacherResource resource = new StudentTeacherResource();
                        resource.setComment(session.getTeacherComment());
                        resource.setTeacher((OutboundTeacher) teacher);
                        return resource;
                    } else {
                        return null;
                    }

                });
    }

}
