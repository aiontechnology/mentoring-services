/*
 * Copyright 2022-2023 Aion Technology LLC
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

import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.assembler.AssemblerSupport;
import io.aiontechnology.mentoring.api.controller.StudentRegistrationController;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.workflow.StudentRegistration;
import io.aiontechnology.mentoring.resource.ProgramAdminResource;
import io.aiontechnology.mentoring.resource.SchoolResource;
import io.aiontechnology.mentoring.resource.StudentRegistrationResource;
import io.aiontechnology.mentoring.resource.TeacherResource;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
public class StudentRegistrationAssembler extends AssemblerSupport<StudentRegistration, StudentRegistrationResource> {

    private final Assembler<School, SchoolResource> schoolAssembler;

    private final Assembler<SchoolPersonRole, ProgramAdminResource> programAdminAssembler;

    private final Assembler<SchoolPersonRole, TeacherResource> teacherAssembler;

    @Override
    protected Optional<StudentRegistrationResource> doMap(StudentRegistration studentRegistration) {
        return Optional.ofNullable(studentRegistration).map(r -> {
            StudentRegistrationResource resource = new StudentRegistrationResource(r);
            resource.setStudentFirstName(r.getStudentFirstName());
            resource.setStudentLastName(r.getStudentLastName());
            resource.setParent1FirstName(r.getParent1FirstName());
            resource.setParent1LastName(r.getParent1LastName());
            resource.setParent1EmailAddress(r.getParent1EmailAddress());
            return resource;
        });
    }

    @Override
    protected Optional<StudentRegistrationResource> doMapWithData(StudentRegistration studentRegistration, Map data) {
        return doMap(studentRegistration)
                .map(r -> {
                    r.setSchool(assembleSchool(data));
                    r.setProgramAdmin(assembleProgramAdmin(data));
                    r.setTeachers(assembleTeachers(data));
                    return r;
                });
    }

    @Override
    protected Set<Link> getLinks(StudentRegistrationResource model, Map data) {
        return Optional.ofNullable(data.get("school"))
                .map(School.class::cast)
                .map(school -> {
                    StudentRegistration studentRegistration = model.getContent();
                    return Set.of(
                            linkTo(StudentRegistrationController.class, school.getId())
                                    .slash(studentRegistration.getId())
                                    .withSelfRel()
                    );
                })
                .orElse(Collections.emptySet());

    }

    private ProgramAdminResource assembleProgramAdmin(Map data) {
        return Optional.ofNullable(data.get("programAdmin"))
                .map(SchoolPersonRole.class::cast)
                .flatMap(programAdminAssembler::map)
                .orElse(null);
    }

    private SchoolResource assembleSchool(Map data) {
        return Optional.ofNullable(data.get("school"))
                .map(School.class::cast)
                .flatMap(schoolAssembler::map)
                .orElse(null);
    }

    private Collection<TeacherResource> assembleTeachers(Map data) {
        return Optional.ofNullable((Collection<SchoolPersonRole>) data.get("teachers"))
                .map(t -> t.stream()
                        .map(teacherAssembler::map)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }

}
