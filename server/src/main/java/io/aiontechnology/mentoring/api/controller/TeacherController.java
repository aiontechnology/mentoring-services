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
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.model.inbound.InboundTeacher;
import io.aiontechnology.mentoring.resource.TeacherResource;
import io.aiontechnology.mentoring.service.RoleService;
import io.aiontechnology.mentoring.service.SchoolService;
import io.aiontechnology.mentoring.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.aiontechnology.mentoring.model.enumeration.RoleType.TEACHER;

/**
 * Controller that vends a REST interface for dealing with teachers.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/teachers")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {

    // Assemblers
    private final Assembler<SchoolPersonRole, TeacherResource> teacherAssembler;

    // Mappers
    private final OneWayMapper<InboundTeacher, SchoolPersonRole> teacherMapper;
    private final OneWayUpdateMapper<InboundTeacher, SchoolPersonRole> teacherUpdateMapper;

    // Services
    private final RoleService roleService;
    private final SchoolService schoolService;
    private final StudentService studentService;

    //Other
    private final EntityManager entityManager;

    /**
     * A REST endpoint for creating a teacher for a particular school.
     *
     * @param schoolId The ID of the school.
     * @param inboundTeacher The model that represents the desired new teacher.
     * @return A model that represents the created teacher.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('teacher:create')")
    public TeacherResource createTeacher(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundTeacher inboundTeacher) {
        log.debug("Creating teacher: {}", inboundTeacher);
        return schoolService.getSchoolById(schoolId)
                .map(school -> Optional.ofNullable(inboundTeacher)
                        .flatMap(teacherMapper::map)
                        .map(school::addRole)
                        .map(roleService::createRole)
                        .flatMap(teacherAssembler::map)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to create teacher")))
                .orElseThrow(() -> new NotFoundException("School not found"));
    }

    /**
     * A REST endpoint for deactivating a teacher.
     *
     * @param studentId The school from which the teacher should be deactivated.
     * @param teacherId The id of the teacher to remove.
     */
    @DeleteMapping("/{teacherId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('teacher:delete')")
    public void deactivateTeacher(@PathVariable("schoolId") UUID studentId, @PathVariable("teacherId") UUID teacherId) {
        log.debug("Deactivating teacher");
        Optional<SchoolPersonRole> teacher = roleService.findRoleById(teacherId);
        teacher.ifPresent(roleService::deactivateRole);

        Optional<School> school = teacher.map(SchoolPersonRole::getSchool);

        SchoolSession currentSession = school
                .map(School::getCurrentSession)
                .orElse(null);

        Collection<Student> students = school
                .map(School::getStudents)
                .orElse(Collections.emptyList());

        students.stream()
                .map(student -> studentService.removeTeacherFromCurrentSession(student, currentSession))
                .forEach(studentService::updateStudent);
    }

    /**
     * A REST endpoint for getting a specific teacher for a particular school.
     *
     * @param schoolId The id of the school.
     * @param teacherId The id of the teacher.
     * @return The personnel if it could be found.
     */
    @GetMapping("/{teacherId}")
    @PreAuthorize("hasAuthority('teacher:read')")
    public TeacherResource getTeacher(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("teacherId") UUID teacherId) {
        return roleService.findRoleById(teacherId)
                .flatMap(teacherAssembler::map)
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for retrieving all teachers.
     *
     * @param schoolId The id of the school.
     * @return A collection of {@link InboundTeacher} instances for the requested school.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('teachers:read')")
    public CollectionModel<TeacherResource> getTeachers(@PathVariable("schoolId") UUID schoolId) {
        log.debug("Getting all teachers for school {}", schoolId);
        var session = entityManager.unwrap(Session.class);
        session.enableFilter("roleType").setParameter("type", TEACHER.toString());
        return schoolService.getSchoolById(schoolId)
                .map(school -> school.getRoles().stream()
                        .map(teacherAssembler::map)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                .map(CollectionModel::of)
                .orElseThrow(() -> new NotFoundException("Requested school not found"));
    }

    /**
     * A REST endpoint for updating a teacher.
     *
     * @param schoolId The id of the school.
     * @param teacherId The id of the teacher to update.
     * @param inboundTeacher The model that represents the updated teacher.
     * @return A model that represents the teacher that has been updated.
     */
    @PutMapping("/{teacherId}")
    @PreAuthorize("hasAuthority('teacher:update')")
    public TeacherResource updateTeacher(@PathVariable("schoolId") UUID schoolId,
            @PathVariable("teacherId") UUID teacherId,
            @RequestBody @Valid InboundTeacher inboundTeacher) {
        return roleService.findRoleById(teacherId)
                .flatMap(role -> teacherUpdateMapper.map(inboundTeacher, role))
                .map(roleService::updateRole)
                .flatMap(teacherAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to update personnel"));
    }

}
