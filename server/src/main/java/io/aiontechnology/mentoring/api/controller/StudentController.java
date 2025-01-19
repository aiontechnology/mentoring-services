/*
 * Copyright 2020-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.controller;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudent;
import io.aiontechnology.mentoring.resource.StudentResource;
import io.aiontechnology.mentoring.service.SchoolService;
import io.aiontechnology.mentoring.service.SchoolSessionService;
import io.aiontechnology.mentoring.service.StudentRegistrationService;
import io.aiontechnology.mentoring.service.StudentSchoolSessionService;
import io.aiontechnology.mentoring.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.REGISTRATION_TIMEOUT_VALUE;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.CREATED;

/**
 * Controller that vends a REST interface for dealing with students.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    // Assemblers
    private final Assembler<Student, StudentResource> studentAssembler;

    // Mappers
    private final OneWayMapper<InboundStudent, Student> studentModelToEntityMapper;
    private final OneWayUpdateMapper<InboundStudent, Student> studentModelToEntityUpdateMapper;
    private final OneWayMapper<InboundStudent, StudentSchoolSession> studentSessionModelToEntityMapper;
    private final OneWayUpdateMapper<InboundStudent, StudentSchoolSession> studentSessionModelToEntityUpdateMapper;

    // Services
    private final SchoolService schoolService;
    private final SchoolSessionService schoolSessionService;
    private final StudentRegistrationService studentRegistrationService;
    private final StudentSchoolSessionService studentSchoolSessionService;
    private final StudentService studentService;

    /**
     * Create a new student. The student is attached to the school's current session. It is not possible to create
     * a student for a historic session.
     *
     * @param schoolId The ID of the school to which the student should belong
     * @param inboundStudent The data that will be used to create the new student.
     * @return The newly created student
     */
    @PostMapping
    @ResponseStatus(CREATED)
    @Transactional
    @PreAuthorize("hasAuthority('student:create')")
    public StudentResource createStudent(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundStudent inboundStudent) {
        log.debug("Creating student {}, for school {}", inboundStudent, schoolId);
        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        SchoolSession currentSession = school.getCurrentSession();
        requireNonNull(currentSession, "Current session is not set");

        Student student = studentModelToEntityMapper.map(inboundStudent)
                .orElseThrow(() -> new NotFoundException("Student was not found"));

        StudentSchoolSession studentSchoolSession = studentSessionModelToEntityMapper.map(inboundStudent)
                .map(ss -> ss.setSession(currentSession))
                .orElseThrow(() -> new IllegalStateException("Mapping of student session failed"));

        student.addStudentSession(studentSchoolSession);
        school.addStudent(student);
        studentService.updateStudent(student);

        studentRegistrationService.startStudentInformationProcess(school, student, inboundStudent.getBaseUri(),
                REGISTRATION_TIMEOUT_VALUE);

        return studentAssembler.map(student).orElse(null);
    }

    /**
     * Deactivate the given student.
     *
     * @param schoolId The school that owns the student that is being deacivated.
     * @param studentId The ID of the student to be deleted.
     */
    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('student:delete')")
    public void deactivateStudent(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("studentId") UUID studentId) {
        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        SchoolSession currentSession = school.getCurrentSession();
        requireNonNull(currentSession, "Current session is not set");

        studentService.getStudentById(studentId, currentSession)
                .flatMap(student -> student.findCurrentSessionForStudent(currentSession))
                .ifPresent(studentSchoolSessionService::deactivateStudent);
    }

    /**
     * Retrieving all students for a school.
     *
     * @param schoolId The id of the school.
     * @return A collection of {@link OutboundStudent} instances for the given school.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('students:read')")
    public CollectionModel<StudentResource> getAllStudentsForSchool(@PathVariable("schoolId") UUID schoolId,
            @RequestParam("session") Optional<UUID> sessionId) {
        log.debug("Getting all students for school: {}, session: {}", schoolId, sessionId
                .map(UUID::toString).orElse("Not provided"));

        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        SchoolSession session = sessionId
                .flatMap(schoolSessionService::getSchoolSessionById)
                .orElse(school.getCurrentSession());
        requireNonNull(session, "No session");
        Map<String, Object> data = Map.of("session", session);

        Collection<StudentResource> studentModels =
                StreamSupport.stream(studentService.getAllStudents(school, session).spliterator(), false)
                        .map(student -> studentAssembler.mapWithData(student, data))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
        return CollectionModel.of(studentModels);
    }

    /**
     * Get a particular student.
     *
     * @param schoolId The ID of the school that owns the student.
     * @param studentId The ID of the desired student.
     * @param sessionId The ID of the desired session.
     * @return The student if it could be found.
     */
    @GetMapping("/{studentId}")
    @PreAuthorize("hasAuthority('student:read')")
    public StudentResource getStudent(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("studentId") UUID studentId, @RequestParam("session") Optional<UUID> sessionId) {
        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        SchoolSession session = sessionId
                .flatMap(schoolSessionService::getSchoolSessionById)
                .orElse(school.getCurrentSession());
        requireNonNull(session, "No session");
        Map<String, Object> data = Map.of("session", session);

        return studentService.getStudentById(studentId, session)
                .flatMap(student -> studentAssembler.mapWithData(student, data))
                .orElseThrow(() -> new NotFoundException("Student was not found"));
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAuthority('student:update')")
    public StudentResource updateStudent(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("studentId") UUID studentId, @RequestBody @Valid InboundStudent inboundStudent) {
        School school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));

        SchoolSession currentSession = school.getCurrentSession();
        requireNonNull(currentSession, "Current session is not set");

        Student student = studentService.getStudentById(studentId, currentSession)
                .orElseThrow(() -> new NotFoundException("Student was not found"));

        StudentSchoolSession currentStudentSession = student.findCurrentSessionForStudent(currentSession)
                .orElseThrow(() -> new NotFoundException("No student session found"));
        studentModelToEntityUpdateMapper.map(inboundStudent, student);
        studentSessionModelToEntityUpdateMapper.map(inboundStudent, currentStudentSession);
        studentService.updateStudent(student);
        return studentAssembler.map(student).orElse(null);
    }

}
