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

package io.aiontechnology.mentoring.configuration;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.assembler.NameableToStringModelMapper;
import io.aiontechnology.mentoring.api.assembler.impl.BookAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.GameAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.InterestAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.MentorAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.PersonAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.PersonnelAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.ProgramAdminAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.SchoolAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.SchoolSessionAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.StudentAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.StudentInformationAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.StudentMentorAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.StudentRegistrationAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.StudentTeacherAssembler;
import io.aiontechnology.mentoring.api.assembler.impl.TeacherAssembler;
import io.aiontechnology.mentoring.api.mapping.tomodel.misc.AddressEntityToModelMapper;
import io.aiontechnology.mentoring.api.mapping.tomodel.misc.PhoneEntityToModelMapper;
import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.entity.Game;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.entity.StudentActivityFocus;
import io.aiontechnology.mentoring.entity.StudentBehavior;
import io.aiontechnology.mentoring.entity.StudentLeadershipSkill;
import io.aiontechnology.mentoring.entity.StudentLeadershipTrait;
import io.aiontechnology.mentoring.entity.StudentPersonRole;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.entity.reference.ActivityFocus;
import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.entity.reference.Phonogram;
import io.aiontechnology.mentoring.entity.reference.Tag;
import io.aiontechnology.mentoring.entity.workflow.StudentInformation;
import io.aiontechnology.mentoring.entity.workflow.StudentRegistration;
import io.aiontechnology.mentoring.feature.workflow.assembler.PostAssessmentWorkflowAssembler;
import io.aiontechnology.mentoring.feature.workflow.model.PostAssessmentWorkflow;
import io.aiontechnology.mentoring.model.outbound.student.OutboundContact;
import io.aiontechnology.mentoring.resource.BookResource;
import io.aiontechnology.mentoring.resource.GameResource;
import io.aiontechnology.mentoring.resource.InterestResource;
import io.aiontechnology.mentoring.resource.MentorResource;
import io.aiontechnology.mentoring.resource.PersonResource;
import io.aiontechnology.mentoring.resource.PersonnelResource;
import io.aiontechnology.mentoring.resource.PostAssessmentWorkflowResource;
import io.aiontechnology.mentoring.resource.ProgramAdminResource;
import io.aiontechnology.mentoring.resource.SchoolResource;
import io.aiontechnology.mentoring.resource.SchoolSessionResource;
import io.aiontechnology.mentoring.resource.StudentInformationResource;
import io.aiontechnology.mentoring.resource.StudentMentorResource;
import io.aiontechnology.mentoring.resource.StudentRegistrationResource;
import io.aiontechnology.mentoring.resource.StudentResource;
import io.aiontechnology.mentoring.resource.StudentTeacherResource;
import io.aiontechnology.mentoring.resource.TeacherResource;
import io.aiontechnology.mentoring.util.PhoneService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Configuration
public class AssemblerConfiguration {

    @Bean
    public NameableToStringModelMapper<ActivityFocus> activityFocusAssembler(
            OneWayMapper<ActivityFocus, String> mapper) {
        return new NameableToStringModelMapper<>(mapper);
    }

    @Bean
    public NameableToStringModelMapper<Behavior> behaviorAssembler(OneWayMapper<Behavior, String> mapper) {
        return new NameableToStringModelMapper<>(mapper);
    }

    @Bean
    public Assembler<Book, BookResource> bookAssembler(
            OneWayCollectionMapper<Interest, String> interestMapper,
            OneWayCollectionMapper<LeadershipSkill, String> leadershipSkillMapper,
            OneWayCollectionMapper<LeadershipTrait, String> leadershipTraitMapper,
            OneWayCollectionMapper<Phonogram, String> phonogramMapper,
            OneWayCollectionMapper<Behavior, String> behaviorMapper,
            OneWayCollectionMapper<Tag, String> tagMapper) {
        return new BookAssembler()
                .withSubMapper("interests", interestMapper)
                .withSubMapper("leadershipSkills", leadershipSkillMapper)
                .withSubMapper("leadershipTraits", leadershipTraitMapper)
                .withSubMapper("phonograms", phonogramMapper)
                .withSubMapper("behaviors", behaviorMapper)
                .withSubMapper("tags", tagMapper);
    }

    @Bean
    public Assembler<Game, GameResource> gameAssembler(
            OneWayCollectionMapper<ActivityFocus, String> activityFocusMapper,
            OneWayCollectionMapper<LeadershipSkill, String> leadershipSkillMapper,
            OneWayCollectionMapper<LeadershipTrait, String> leadershipTraitMapper) {
        return new GameAssembler()
                .withSubMapper("activityFocuses", activityFocusMapper)
                .withSubMapper("leadershipSkills", leadershipSkillMapper)
                .withSubMapper("leadershipTraits", leadershipTraitMapper);
    }

    @Bean
    public Assembler<Interest, InterestResource> interestAssembler() {
        return new InterestAssembler();
    }

    @Bean
    public NameableToStringModelMapper<LeadershipSkill> leadershipSkillAssembler(OneWayMapper<LeadershipSkill,
            String> mapper) {
        return new NameableToStringModelMapper<>(mapper);
    }

    @Bean
    public NameableToStringModelMapper<LeadershipTrait> leadershipTraitAssembler(OneWayMapper<LeadershipTrait,
            String> mapper) {
        return new NameableToStringModelMapper<>(mapper);
    }

    @Bean
    public Assembler<SchoolPersonRole, MentorResource> mentorAssembler(
            PhoneService phoneService) {
        return new MentorAssembler(phoneService);
    }

    @Bean
    public Assembler<Person, PersonResource> personAssembler(
            PhoneEntityToModelMapper phoneMapper) {
        return new PersonAssembler()
                .withSubMapper("workPhone", phoneMapper)
                .withSubMapper("cellPhone", phoneMapper);
    }

    @Bean
    public Assembler<SchoolPersonRole, PersonnelResource> personnelAssembler(
            PhoneService phoneService) {
        return new PersonnelAssembler(phoneService);
    }

    @Bean
    public NameableToStringModelMapper<Phonogram> phonogramAssembler(OneWayMapper<Phonogram, String> mapper) {
        return new NameableToStringModelMapper<>(mapper);
    }

    @Bean
    public Assembler<PostAssessmentWorkflow, PostAssessmentWorkflowResource> postAssessmentWorkflowAssembler() {
        return new PostAssessmentWorkflowAssembler();
    }

    @Bean
    public Assembler<SchoolPersonRole, ProgramAdminResource> programAdminAssembler(
            PhoneService phoneService) {
        return new ProgramAdminAssembler(phoneService);
    }

    @Bean
    public Assembler<School, SchoolResource> schoolAssembler(
            Assembler<SchoolSession, SchoolSessionResource> schoolSessionAssembler,
            AddressEntityToModelMapper addressMapper,
            PhoneService phoneService) {
        return new SchoolAssembler(schoolSessionAssembler, addressMapper, phoneService);
    }

    @Bean
    public Assembler<SchoolSession, SchoolSessionResource> schoolSessionAssembler() {
        return new SchoolSessionAssembler();
    }

    @Bean
    public Assembler<Student, StudentResource> studentAssembler(
            Assembler<StudentSchoolSession, StudentTeacherResource> teacherAssembler,
            Assembler<StudentSchoolSession, StudentMentorResource> mentorAssembler,
            OneWayCollectionMapper<Interest, String> interestsMapper,
            OneWayCollectionMapper<StudentActivityFocus, String> activityFocusMapper,
            OneWayCollectionMapper<StudentBehavior, String> behaviorsMapper,
            OneWayCollectionMapper<StudentLeadershipSkill, String> leadershipSkillsMapper,
            OneWayCollectionMapper<StudentLeadershipTrait, String> leadershipTraitsMapper,
            OneWayCollectionMapper<StudentPersonRole, OutboundContact> contactsMapper) {
        return new StudentAssembler()
                .withSubMapper("teacher", teacherAssembler)
                .withSubMapper("mentor", mentorAssembler)
                .withSubMapper("interests", interestsMapper)
                .withSubMapper("activityFocuses", activityFocusMapper)
                .withSubMapper("behaviors", behaviorsMapper)
                .withSubMapper("leadershipSkills", leadershipSkillsMapper)
                .withSubMapper("leadershipTraits", leadershipTraitsMapper)
                .withSubMapper("contacts", contactsMapper);
    }

    @Bean
    public Assembler<StudentInformation, StudentInformationResource> studentInformationAssembler() {
        return new StudentInformationAssembler();
    }

    @Bean
    public Assembler<StudentSchoolSession, StudentMentorResource> studentMentorAssembler(
            Assembler<SchoolPersonRole, MentorResource> mentorAssembler) {
        return new StudentMentorAssembler()
                .withSubMapper("mentor", mentorAssembler);
    }

    @Bean
    public Assembler<StudentRegistration, StudentRegistrationResource> studentRegistrationAssembler(
            Assembler<School, SchoolResource> schoolAssembler,
            Assembler<SchoolPersonRole, ProgramAdminResource> programAdminAssembler,
            Assembler<SchoolPersonRole, TeacherResource> teacherAssembler) {
        return new StudentRegistrationAssembler(schoolAssembler, programAdminAssembler, teacherAssembler);
    }

    @Bean
    public Assembler<StudentSchoolSession, StudentTeacherResource> studentTeacherAssembler(
            Assembler<SchoolPersonRole, TeacherResource> teacherAssembler) {
        return new StudentTeacherAssembler()
                .withSubMapper("teacher", teacherAssembler);
    }

    @Bean
    public NameableToStringModelMapper<Tag> tagAssembler(OneWayMapper<Tag, String> mapper) {
        return new NameableToStringModelMapper<>(mapper);
    }

    @Bean
    public Assembler<SchoolPersonRole, TeacherResource> teacherAssembler(PhoneService phoneService) {
        return new TeacherAssembler(phoneService);
    }

}
