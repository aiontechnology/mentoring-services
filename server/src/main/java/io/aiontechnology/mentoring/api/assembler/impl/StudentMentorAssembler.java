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
import io.aiontechnology.mentoring.model.outbound.OutboundMentor;
import io.aiontechnology.mentoring.resource.StudentMentorResource;

import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class StudentMentorAssembler extends AssemblerSupport<StudentSchoolSession, StudentMentorResource> {

    @Override
    protected Optional<StudentMentorResource> doMap(StudentSchoolSession studentSchoolSession) {
        return Optional.ofNullable(studentSchoolSession)
                .map(session -> {

                    Object mentor = getSubMapper("mentor")
                            .map(session.getMentor())
                            .orElse(null);
                    if(mentor != null) {
                        StudentMentorResource resource = new StudentMentorResource();
                        resource.setMentor((OutboundMentor) mentor);
                        return resource;
                    } else {
                        return null;
                    }

                });
    }

}
