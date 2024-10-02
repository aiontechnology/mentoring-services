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

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayToCollectionUpdateMapper;
import io.aiontechnology.mentoring.entity.StudentBehavior;
import io.aiontechnology.mentoring.entity.StudentLeadershipSkill;
import io.aiontechnology.mentoring.entity.StudentLeadershipTrait;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentBehavior;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipSkill;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipTrait;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentTeacher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.aiontechnology.mentoring.model.enumeration.ResourceLocation.ONLINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Disabled
public class StudentModelToSessionEntityUpdateMapperTest {

    @Test
    void testMapProperties() {
        // set up the fixture
        Integer grade = 1;
        String preferredTime = "PREFERRED";
        String actualTime = "ACTUAL";
        Date startDate = new Date();
        ResourceLocation location = ONLINE;
        Boolean isMediaReleaseSigned = true;

        InboundStudent inboundStudent = InboundStudent.builder()
                .withGrade(grade)
                .withPreferredTime(preferredTime)
                .withActualTime(actualTime)
                .withStartDate(startDate)
                .withLocation(location)
                .withMediaReleaseSigned(isMediaReleaseSigned)
                .build();

        StudentSchoolSession StudentSchoolSession = new StudentSchoolSession();

        StudentModelToSessionEntityUpdateMapper mapper = new StudentModelToSessionEntityUpdateMapper(null, null, null
                , null, null, null, null);

        // execute the SUT
        Optional<StudentSchoolSession> result = mapper.map(inboundStudent, StudentSchoolSession);

        // validation
        assertThat(result).isPresent();
        StudentSchoolSession resultStudentSchoolSession = result.get();
        assertThat(resultStudentSchoolSession.getGrade()).isEqualTo(grade);
        assertThat(resultStudentSchoolSession.getPreferredTime()).isEqualTo(preferredTime);
        assertThat(resultStudentSchoolSession.getActualTime()).isEqualTo(actualTime);
        assertThat(resultStudentSchoolSession.getStartDate()).isEqualTo(startDate);
        assertThat(resultStudentSchoolSession.getIsMediaReleaseSigned()).isEqualTo(isMediaReleaseSigned);
    }

    @Test
    void testMapInterests() {
        // set up the fixture
        InboundStudent inboundStudent = mock(InboundStudent.class);
        Set<String> interestStrings = Set.of("ONE", "TWO");
        when(inboundStudent.getInterests()).thenReturn(interestStrings);

        StudentSchoolSession studentSchoolSession = mock(StudentSchoolSession.class);

        OneWayCollectionMapper<String, Interest> interestMapper = mock(OneWayCollectionMapper.class);
        List<Interest> interests = Arrays.asList(new Interest(), new Interest());
        when(interestMapper.map(interestStrings)).thenReturn(Optional.of(interests));

        StudentModelToSessionEntityUpdateMapper mapper = new StudentModelToSessionEntityUpdateMapper(interestMapper,
                null, null, null, null, null, null);

        // execute the SUT
        mapper.map(inboundStudent, studentSchoolSession);

        // validation
        verify(interestMapper).map(interestStrings);
        verify(studentSchoolSession).setInterests(interests);
    }

    @Test
    void testMapBehaviors() {
        // set up the fixture
        URI teacherURI = URI.create("http://test.com");

        InboundStudentTeacher inboundStudentTeacher = mock(InboundStudentTeacher.class);
        when(inboundStudentTeacher.getUri()).thenReturn(teacherURI);

        InboundStudent inboundStudent = mock(InboundStudent.class);
        when(inboundStudent.getTeacher()).thenReturn(inboundStudentTeacher);
        Set<String> behaviorStrings = Set.of("ONE", "TWO");
        when(inboundStudent.getBehaviors()).thenReturn(behaviorStrings);

        StudentSchoolSession studentSchoolSession = mock(StudentSchoolSession.class);
        Collection<StudentBehavior> studentBehaviors = new ArrayList<>();
        when(studentSchoolSession.getStudentBehaviors()).thenReturn(studentBehaviors);

        OneWayToCollectionUpdateMapper<InboundStudentBehavior, StudentBehavior> behaviorMapper =
                mock(OneWayToCollectionUpdateMapper.class);
        List<Behavior> behaviors = Arrays.asList(new Behavior(), new Behavior());
        when(behaviorMapper.map(any(), eq(studentBehaviors))).thenReturn(studentBehaviors);

        StudentModelToSessionEntityUpdateMapper mapper = new StudentModelToSessionEntityUpdateMapper(null,
                null, null, behaviorMapper, null, null, null);

        // execute the SUT
        mapper.map(inboundStudent, studentSchoolSession);

        // validation
        verify(behaviorMapper).map(any(InboundStudentBehavior.class), eq(studentBehaviors));
        verify(studentSchoolSession).setStudentBehaviors(studentBehaviors);
    }

//    @Test
//    void testMapMentor() {
//        // set up the fixture
//        InboundStudent inboundStudent = mock(InboundStudent.class);
//        InboundStudentMentor inboundStudentMentor = InboundStudentMentor.builder().build();
//        when(inboundStudent.getMentor()).thenReturn(inboundStudentMentor);
//
//        StudentSchoolSession studentSchoolSession = mock(StudentSchoolSession.class);
//        StudentMentor studentMentor = new StudentMentor();
//        when(studentSchoolSession.getMentor()).thenReturn(studentMentor);
//
//        OneWayUpdateMapper<InboundStudentMentor, StudentMentor> mentorMapper = mock(OneWayUpdateMapper.class);
//        when(mentorMapper.map(inboundStudentMentor, studentMentor)).thenReturn(Optional.of(studentMentor));
//
//        StudentModelToSessionEntityUpdateMapper mapper = new StudentModelToSessionEntityUpdateMapper(null, null,
//                null, mentorMapper, null, null);
//
//        // execute the SUT
//        mapper.map(inboundStudent, studentSchoolSession);
//
//        // validation
//        verify(mentorMapper).map(inboundStudentMentor, studentMentor);
//        verify(studentSchoolSession).setMentor(studentMentor);
//    }

    @Test
    void testMapLeadershipSkills() {
        // set up the fixture
        URI teacherURI = URI.create("http://test.com");

        InboundStudentTeacher inboundStudentTeacher = mock(InboundStudentTeacher.class);
        when(inboundStudentTeacher.getUri()).thenReturn(teacherURI);

        InboundStudent inboundStudent = mock(InboundStudent.class);
        when(inboundStudent.getTeacher()).thenReturn(inboundStudentTeacher);
        Set<String> leadershipSkillStrings = Set.of("ONE", "TWO");
        when(inboundStudent.getLeadershipSkills()).thenReturn(leadershipSkillStrings);

        StudentSchoolSession studentSchoolSession = mock(StudentSchoolSession.class);
        Collection<StudentLeadershipSkill> studentLeadershipSkills = new ArrayList<>();
        when(studentSchoolSession.getStudentLeadershipSkills()).thenReturn(studentLeadershipSkills);

        OneWayToCollectionUpdateMapper<InboundStudentLeadershipSkill, StudentLeadershipSkill> leadershipSkillMapper =
                mock(OneWayToCollectionUpdateMapper.class);
        List<Interest> interests = Arrays.asList(new Interest(), new Interest());
        when(leadershipSkillMapper.map(any(), eq(studentLeadershipSkills))).thenReturn(studentLeadershipSkills);

        StudentModelToSessionEntityUpdateMapper mapper = new StudentModelToSessionEntityUpdateMapper(null, null, null,
                null, leadershipSkillMapper, null, null);

        // execute the SUT
        mapper.map(inboundStudent, studentSchoolSession);

        // validation
        verify(leadershipSkillMapper).map(any(InboundStudentLeadershipSkill.class), eq(studentLeadershipSkills));
        verify(studentSchoolSession).setStudentLeadershipSkills(studentLeadershipSkills);
    }

    @Test
    void testMapLeadershipTraits() {
        // set up the fixture
        URI teacherURI = URI.create("http://test.com");

        InboundStudentTeacher inboundStudentTeacher = mock(InboundStudentTeacher.class);
        when(inboundStudentTeacher.getUri()).thenReturn(teacherURI);

        InboundStudent inboundStudent = mock(InboundStudent.class);
        when(inboundStudent.getTeacher()).thenReturn(inboundStudentTeacher);
        Set<String> leadershipTraitStrings = Set.of("ONE", "TWO");
        when(inboundStudent.getLeadershipTraits()).thenReturn(leadershipTraitStrings);

        StudentSchoolSession studentSchoolSession = mock(StudentSchoolSession.class);
        Collection<StudentLeadershipTrait> studentLeadershipTraits = new ArrayList<>();
        when(studentSchoolSession.getStudentLeadershipTraits()).thenReturn(studentLeadershipTraits);

        OneWayToCollectionUpdateMapper<InboundStudentLeadershipTrait, StudentLeadershipTrait> leadershipTraitMapper =
                mock(OneWayToCollectionUpdateMapper.class);
        List<Interest> interests = Arrays.asList(new Interest(), new Interest());
        when(leadershipTraitMapper.map(any(), eq(studentLeadershipTraits))).thenReturn(studentLeadershipTraits);

        StudentModelToSessionEntityUpdateMapper mapper = new StudentModelToSessionEntityUpdateMapper(null, null, null,
                null, null, leadershipTraitMapper, null);

        // execute the SUT
        mapper.map(inboundStudent, studentSchoolSession);

        // validation
        verify(leadershipTraitMapper).map(any(InboundStudentLeadershipTrait.class), eq(studentLeadershipTraits));
        verify(studentSchoolSession).setStudentLeadershipTraits(studentLeadershipTraits);
    }

//    @Test
//    void testMapTeacher() {
//        // set up the fixture
//        InboundStudent inboundStudent = mock(InboundStudent.class);
//        InboundStudentTeacher inboundStudentTeacher = InboundStudentTeacher.builder().build();
//        when(inboundStudent.getTeacher()).thenReturn(inboundStudentTeacher);
//
//        StudentSchoolSession studentSchoolSession = mock(StudentSchoolSession.class);
//        StudentTeacher studentTeacher = new StudentTeacher();
//        when(studentSchoolSession.getTeacher()).thenReturn(studentTeacher);
//
//        OneWayUpdateMapper<InboundStudentTeacher, StudentTeacher> teacherMapper = mock(OneWayUpdateMapper.class);
//        when(teacherMapper.map(inboundStudentTeacher, studentTeacher)).thenReturn(Optional.of(studentTeacher));
//
//        StudentModelToSessionEntityUpdateMapper mapper = new StudentModelToSessionEntityUpdateMapper(null, null,
//                null, teacherMapper, null, null);
//
//        // execute the SUT
//        mapper.map(inboundStudent, studentSchoolSession);
//
//        // validation
//        verify(teacherMapper).map(inboundStudentTeacher, studentTeacher);
//        verify(studentSchoolSession).setTeacher(studentTeacher);
//    }

}
