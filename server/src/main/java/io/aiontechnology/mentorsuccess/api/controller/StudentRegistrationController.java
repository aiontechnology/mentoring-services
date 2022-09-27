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

package io.aiontechnology.mentorsuccess.api.controller;

import io.aiontechnology.mentorsuccess.api.assembler.Assembler;
import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.entity.workflow.StudentRegistration;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentRegistration;
import io.aiontechnology.mentorsuccess.resource.StudentRegistrationResource;
import io.aiontechnology.mentorsuccess.service.SchoolService;
import io.aiontechnology.mentorsuccess.service.StudentRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.model.enumeration.RoleType.TEACHER;

/**
 * Controller that vends a REST interface for registering students.
 *
 * @author Whitney Hunter
 * @since 1.10.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/registrations")
@RequiredArgsConstructor
@Slf4j
public class StudentRegistrationController {

    // Assemblers
    private final Assembler<StudentRegistration, StudentRegistrationResource> studentRegistrationAssembler;

    // Services
    private final StudentRegistrationService studentRegistrationService;
    private final SchoolService schoolService;

    //Other
    private final EntityManager entityManager;

    @GetMapping("/{registrationId}")
    public StudentRegistrationResource findRegistration(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("registrationId") UUID registrationId) {
        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        var session = entityManager.unwrap(Session.class);
        session.enableFilter("roleType").setParameter("type", TEACHER.toString());
        Collection<SchoolPersonRole> teachers = school.getRoles();

        Map<String, Object> data = Map.of("school", school, "teachers", teachers);

        return studentRegistrationService.findWorkflowById(registrationId)
                .flatMap(r -> studentRegistrationAssembler.mapWithData(r, data))
                .orElseThrow(() -> new NotFoundException("Student registration was not found"));
    }

    @PutMapping("/{registrationId}")
    public InboundStudentRegistration handleRegistration(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("registrationId") UUID registrationId,
            @RequestBody @Valid InboundStudentRegistration studentRegistration) {
        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        studentRegistrationService.processRegistration(school.getId(), registrationId, studentRegistration);
        return studentRegistration;
    }

}
