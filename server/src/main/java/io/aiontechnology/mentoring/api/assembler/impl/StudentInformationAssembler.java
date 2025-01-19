/*
 * Copyright 2023 Aion Technology LLC
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
import io.aiontechnology.mentoring.api.controller.StudentInformationController;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.workflow.StudentInformation;
import io.aiontechnology.mentoring.resource.StudentInformationResource;
import org.springframework.hateoas.Link;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.REGISTRATION;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class StudentInformationAssembler extends AssemblerSupport<StudentInformation, StudentInformationResource> {

    @Override
    protected Optional<StudentInformationResource> doMapWithData(StudentInformation student, Map data) {
        return Optional.ofNullable(student).map(s -> {
            StudentInformationResource resource = new StudentInformationResource(s);
            resource.setStudentName(s.getStudentName());
            return resource;
        });
    }

    @Override
    protected Set<Link> getLinks(StudentInformationResource model, Map data) {
        School school = (School) data.get("school");
        Student student = (Student) data.get("student");
        UUID registrationId = (UUID) data.get(REGISTRATION);

        return Set.of(
                linkTo(StudentInformationController.class, school.getId(), student.getId())
                        .slash(registrationId.toString())
                        .withSelfRel()
        );
    }

}
