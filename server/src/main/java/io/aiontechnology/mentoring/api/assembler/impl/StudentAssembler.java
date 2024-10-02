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

import io.aiontechnology.mentoring.api.assembler.AssemblerSupport;
import io.aiontechnology.mentoring.api.controller.StudentController;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.model.outbound.student.OutboundContact;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentMentor;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentTeacher;
import io.aiontechnology.mentoring.resource.StudentResource;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class StudentAssembler extends AssemblerSupport<Student, StudentResource> {

    @Override
    protected Optional<StudentResource> doMap(Student student) {
        return doMapWithData(student, Collections.emptyMap());
    }

    @Override
    protected Optional<StudentResource> doMapWithData(Student student, Map data) {
        SchoolSession schoolSession = Optional.ofNullable(data.get("session"))
                .map(SchoolSession.class::cast)
                .orElse(student.getSchool().getCurrentSession());
        StudentSchoolSession studentSchoolSession = student.findCurrentSessionForStudent(schoolSession)
                .orElseThrow(() -> new NotFoundException("No student session found"));

        StudentResource resource = new StudentResource(student);

        resource.setId(student.getId());
        resource.setStudentId(student.getStudentId());
        resource.setFirstName(student.getFirstName());
        resource.setLastName(student.getLastName());

        resource.setGrade(studentSchoolSession.getGrade());
        resource.setPreferredTime(studentSchoolSession.getPreferredTime());
        resource.setActualTime(studentSchoolSession.getActualTime());
        resource.setStartDate(studentSchoolSession.getStartDate());
        resource.setLocation(studentSchoolSession.getLocation());
        resource.setRegistrationSigned(studentSchoolSession.getIsRegistrationSigned());
        resource.setMediaReleaseSigned(studentSchoolSession.getIsMediaReleaseSigned());
        resource.setPreBehavioralAssessment(studentSchoolSession.getPreBehavioralAssessment());
        resource.setPostBehavioralAssessment(studentSchoolSession.getPostBehavioralAssessment());
        resource.setTeacherInfoWorkflowAllowed(
                studentSchoolSession.getStartedInfoFlowId() == null &&
                        studentSchoolSession.getCompletedInfoFlowId() == null
        );

        Object teacher = getSubMapper("teacher")
                .map(studentSchoolSession)
                .orElse(null);
        resource.setTeacher((OutboundStudentTeacher) teacher);

        Object mentor = getSubMapper("mentor")
                .map(studentSchoolSession)
                .orElse(null);
        resource.setMentor((OutboundStudentMentor) mentor);

        Object interests = getSubMapper("interests")
                .map(studentSchoolSession.getInterests())
                .orElse(Collections.emptyList());
        resource.setInterests((Collection<String>) interests);

        Object activityFocuses = getSubMapper("activityFocuses")
                .map(studentSchoolSession.getStudentActivityFocuses())
                .orElse(Collections.emptyList());
        resource.setActivityFocuses((Collection<String>) activityFocuses);

        Object behaviors = getSubMapper("behaviors")
                .map(studentSchoolSession.getStudentBehaviors())
                .orElse(Collections.emptyList());
        resource.setBehaviors((Collection<String>) behaviors);

        Object leadershipSkills = getSubMapper("leadershipSkills")
                .map(studentSchoolSession.getStudentLeadershipSkills())
                .orElse(Collections.emptyList());
        resource.setLeadershipSkills((Collection<String>) leadershipSkills);

        Object leadershipTraits = getSubMapper("leadershipTraits")
                .map(studentSchoolSession.getStudentLeadershipTraits())
                .orElse(Collections.emptyList());
        resource.setLeadershipTraits((Collection<String>) leadershipTraits);

        Object contacts = getSubMapper("contacts")
                .map(student.getStudentPersonRoles())
                .orElse(Collections.emptyList());
        resource.setContacts((Collection<OutboundContact>) contacts);

        return Optional.of(resource);
    }

    @Override
    protected Set<Link> getLinks(StudentResource model) {
        Student student = model.getContent();
        return Set.of(
                linkTo(StudentController.class, student.getSchool().getId()).slash(student.getId()).withSelfRel());
    }

}
