/*
 * Copyright 2023-2024 Aion Technology LLC
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
import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.entity.StudentSchoolSession;
import io.aiontechnology.mentorsuccess.entity.workflow.StudentInformation;
import io.aiontechnology.mentorsuccess.model.inbound.InboundBaseUri;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentInformation;
import io.aiontechnology.mentorsuccess.resource.StudentInformationResource;
import io.aiontechnology.mentorsuccess.service.SchoolService;
import io.aiontechnology.mentorsuccess.service.StudentRegistrationService;
import io.aiontechnology.mentorsuccess.service.StudentService;
import io.aiontechnology.mentorsuccess.workflow.StudentWorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT_VALUE;

/**
 * Controller class for retrieving, starting and updating student registrations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/students/{studentId}/registrations")
@RequiredArgsConstructor
public class StudentInformationController {

    /** @see io.aiontechnology.mentorsuccess.api.assembler.impl.StudentInformationAssembler */
    private final Assembler<StudentInformation, StudentInformationResource> studentInformationAssembler;

    // Services
    private final SchoolService schoolService;
    private final StudentRegistrationService studentRegistrationService;
    private final StudentService studentService;
    private final StudentWorkflowService studentWorkflowService;

    /**
     * Retrieves the registration information for a student in a school.
     *
     * @param schoolId The ID of the school.
     * @param studentId The ID of the student.
     * @param registrationId The ID of the registration.
     * @return The StudentInformationResource object containing the registration information.
     * @throws NotFoundException if the school or student is not found, or if the student information is not found.
     */
    @GetMapping("/{registrationId}")
    public StudentInformationResource findRegistration(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("studentId") UUID studentId, @PathVariable("registrationId") UUID registrationId) {
        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));
        SchoolSession currentSession = school.getCurrentSession();

        Student student = studentService.getStudentById(studentId, currentSession)
                .orElse(null);

        Map<String, Object> data = Map.of(
                "school", school,
                "student", student,
                REGISTRATION, registrationId
        );

        return studentWorkflowService.findStudentInformationWorkflowById(registrationId)
                .flatMap(s -> studentInformationAssembler.mapWithData(s, data))
                .orElseThrow(() -> new NotFoundException("Student information was not found"));
    }

    /**
     * Starts the registration process for a student in a school.
     *
     * @param schoolId The ID of the school.
     * @param studentId The ID of the student.
     * @param baseUri The base URI for the registration process.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('school:update')")
    public void startRegistration(@PathVariable("schoolId") UUID schoolId, @PathVariable("studentId") UUID studentId,
            @RequestBody @Valid InboundBaseUri baseUri) {
        SchoolSession currentSession = schoolService.getSchoolById(schoolId)
                .map(School::getCurrentSession)
                .orElseThrow();
        studentService.getStudentById(studentId, currentSession)
                .flatMap(student -> student.findCurrentSessionForStudent(currentSession))
                .map(StudentSchoolSession::getTeacher)
                .ifPresent(teacher ->
                        studentRegistrationService.startStudentInformationProcess(schoolId.toString(),
                                studentId.toString(),
                                teacher.getId().toString(), baseUri.getUri(), REGISTRATION_TIMEOUT_VALUE)
                );
    }

    /**
     * Updates the registration information for a specific student in a school.
     *
     * @param schoolId The ID of the school where the student is enrolled
     * @param studentId The ID of the student
     * @param registrationId The ID of the registration to be updated
     * @param studentInformation The updated student information
     * @return The updated student registration information
     */
    @PutMapping("/{registrationId}")
    public InboundStudentInformation updateRegistration(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("studentId") UUID studentId, @PathVariable("registrationId") UUID registrationId,
            @RequestBody InboundStudentInformation studentInformation) {
        log.debug("==> Updating registration: {}", studentInformation);
        studentRegistrationService.processStudentInformation(schoolId, studentId, registrationId, studentInformation);
        return studentInformation;
    }

}
