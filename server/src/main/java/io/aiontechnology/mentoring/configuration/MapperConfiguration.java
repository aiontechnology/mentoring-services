/*
 * Copyright 2020-2023 Aion Technology LLC
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
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.atlas.mapping.impl.SimpleOneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.impl.UpdateMapperBasedOneWayMapper;
import io.aiontechnology.atlas.synchronization.CollectionSynchronizer;
import io.aiontechnology.atlas.synchronization.impl.SimpleCollectionSynchronizer;
import io.aiontechnology.mentoring.api.mapping.toentity.reference.FunctionBasedModelToEntityMapper;
import io.aiontechnology.mentoring.api.mapping.tomodel.reference.NameableToStringMapper;
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
import io.aiontechnology.mentoring.model.inbound.InboundBook;
import io.aiontechnology.mentoring.model.inbound.InboundGame;
import io.aiontechnology.mentoring.model.inbound.InboundMentor;
import io.aiontechnology.mentoring.model.inbound.InboundPerson;
import io.aiontechnology.mentoring.model.inbound.InboundPersonnel;
import io.aiontechnology.mentoring.model.inbound.InboundProgramAdmin;
import io.aiontechnology.mentoring.model.inbound.InboundSchool;
import io.aiontechnology.mentoring.model.inbound.InboundSchoolSession;
import io.aiontechnology.mentoring.model.inbound.InboundTeacher;
import io.aiontechnology.mentoring.model.inbound.student.InboundContact;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentSchoolSession;
import io.aiontechnology.mentoring.model.outbound.student.OutboundContact;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentSchoolSession;
import io.aiontechnology.mentoring.service.ActivityFocusService;
import io.aiontechnology.mentoring.service.BehaviorService;
import io.aiontechnology.mentoring.service.InterestService;
import io.aiontechnology.mentoring.service.LeadershipSkillService;
import io.aiontechnology.mentoring.service.LeadershipTraitService;
import io.aiontechnology.mentoring.service.PhonogramService;
import io.aiontechnology.mentoring.service.TagService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Spring configuration class to establish mappers.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Configuration
public class MapperConfiguration {

    /*
     * AbstractReference
     */
    @Bean
    public NameableToStringMapper<ActivityFocus> activityFocusToStringMapper() {
        return new NameableToStringMapper<>();
    }

    @Bean
    public NameableToStringMapper<Behavior> behaviorToStringMapper() {
        return new NameableToStringMapper<>();
    }

    @Bean
    public NameableToStringMapper<Interest> interestToStringMapper() {
        return new NameableToStringMapper<>();
    }

    @Bean
    public NameableToStringMapper<LeadershipSkill> leadershipSkillToStringMapper() {
        return new NameableToStringMapper<>();
    }

    @Bean
    public NameableToStringMapper<LeadershipTrait> leadershipTraitToStringMapper() {
        return new NameableToStringMapper<>();
    }

    @Bean
    public NameableToStringMapper<Phonogram> phonogramToStringMapper() {
        return new NameableToStringMapper<>();
    }

    @Bean
    public NameableToStringMapper<Tag> tagToStringMapper() {
        return new NameableToStringMapper<>();
    }

    /*
     * ActivityFocus
     */

    @Bean
    public OneWayMapper<String, ActivityFocus> stringToActivityFocusMapper(ActivityFocusService activityFocusService) {
        return new FunctionBasedModelToEntityMapper<>(activityFocusService::findActivityFocusByName);
    }

    @Bean
    public OneWayCollectionMapper<String, ActivityFocus> activityFocusModelToEntityOneWayCollectionMapper(
            OneWayMapper<String, ActivityFocus> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    @Bean
    public OneWayCollectionMapper<ActivityFocus, String> activityFocusEntityToModelOneWayCollectionMapper(
            OneWayMapper<ActivityFocus, String> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * Behavior
     */

    @Bean
    public OneWayMapper<String, Behavior> stringToBehaviorMapper(BehaviorService behaviorService) {
        return new FunctionBasedModelToEntityMapper<>(behaviorService::findBehaviorByName);
    }

    @Bean
    public OneWayCollectionMapper<String, Behavior> behaviorModelToEntityOneWayCollectionMapper(
            OneWayMapper<String, Behavior> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    @Bean
    public OneWayCollectionMapper<Behavior, String> behaviorEntityToModelOneWayCollectionMapper(
            OneWayMapper<Behavior, String> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * Book
     */

    @Bean
    public OneWayMapper<InboundBook, Book> bookModelToEntityMapper(OneWayUpdateMapper<InboundBook, Book> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, Book.class);
    }

    /*
     * Game
     */

    @Bean
    public OneWayMapper<InboundGame, Game> gameModelToEntityMapper(OneWayUpdateMapper<InboundGame, Game> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, Game.class);
    }

    /*
     * Interest
     */

    @Bean
    public OneWayMapper<String, Interest> stringToInterestMapper(InterestService interestService) {
        return new FunctionBasedModelToEntityMapper<>(interestService::findInterestByName);
    }

    @Bean
    public OneWayCollectionMapper<String, Interest> interestModelToEntityOneWayCollectionMapper(
            OneWayMapper<String, Interest> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    @Bean
    public OneWayCollectionMapper<Interest, String> interestEntityToStringOneWayCollectionMapper(
            OneWayMapper<Interest, String> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * LeadershipSkill
     */

    @Bean
    public OneWayMapper<String, LeadershipSkill> stringToLeadershipSkillMapper(LeadershipSkillService leadershipSkillService) {
        return new FunctionBasedModelToEntityMapper<>(leadershipSkillService::findLeadershipSkillByName);
    }

    @Bean
    public OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityOneWayCollectionMapper(
            OneWayMapper<String, LeadershipSkill> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    @Bean
    public OneWayCollectionMapper<LeadershipSkill, String> leadershipSkillEntityToModelOneWayCollectionMapper(
            OneWayMapper<LeadershipSkill, String> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * LeadershipTrait
     */

    @Bean
    public OneWayMapper<String, LeadershipTrait> stringToLeadershipTraitMapper(LeadershipTraitService leadershipTraitService) {
        return new FunctionBasedModelToEntityMapper<>(leadershipTraitService::findLeadershipTraitByName);
    }

    @Bean
    public OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityOneWayCollectionMapper(
            OneWayMapper<String, LeadershipTrait> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    @Bean
    public OneWayCollectionMapper<LeadershipTrait, String> leadershipTraitEntityToModelOneWayCollectionMapper(
            OneWayMapper<LeadershipTrait, String> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * Mentor
     */

    @Bean
    public OneWayMapper<InboundMentor, SchoolPersonRole> mentorModelToEntityMapper(
            OneWayUpdateMapper<InboundMentor, SchoolPersonRole> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, SchoolPersonRole.class);
    }

    /*
     * Person
     */

    @Bean
    public OneWayMapper<InboundPerson, Person> personModelToEntityMapper(OneWayUpdateMapper<InboundPerson, Person> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, Person.class);
    }

    /*
     * Personnel
     */

    @Bean
    public OneWayMapper<InboundPersonnel, SchoolPersonRole> personnelModelToEntityMapper(OneWayUpdateMapper<InboundPersonnel, SchoolPersonRole> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, SchoolPersonRole.class);
    }

    /*
     * Phonogram
     */

    @Bean
    public OneWayMapper<String, Phonogram> stringToPhonogramMapper(PhonogramService phonogramService) {
        return new FunctionBasedModelToEntityMapper<>(phonogramService::findPhonogramByName);
    }

    @Bean
    public OneWayCollectionMapper<String, Phonogram> phonogramModelToEntityOneWayCollectionMapper(
            OneWayMapper<String, Phonogram> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    @Bean
    public OneWayCollectionMapper<Phonogram, String> phonogramEntityToModelOneWayCollectionMapper(
            OneWayMapper<Phonogram, String> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * ProgramAdmin
     */

    @Bean
    public OneWayMapper<Pair<InboundProgramAdmin, UUID>, SchoolPersonRole> programAdminModelToEntityMapper(
            OneWayUpdateMapper<Pair<InboundProgramAdmin, UUID>, SchoolPersonRole> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, SchoolPersonRole.class);
    }

    /*
     * School
     */

    @Bean
    public OneWayMapper<InboundSchool, School> schoolModelToEntityMapper(OneWayUpdateMapper<InboundSchool, School> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, School.class);
    }

    /*
     * SchoolSession
     */

    @Bean
    public OneWayMapper<InboundSchoolSession, SchoolSession> schoolSessionModelToEntityMapper(
            OneWayUpdateMapper<InboundSchoolSession, SchoolSession> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, SchoolSession.class);
    }

    @Bean
    public OneWayMapper<InboundStudentSchoolSession, StudentSchoolSession> studentSchoolSessionModelToEntityMapper(
            OneWayUpdateMapper<InboundStudentSchoolSession, StudentSchoolSession> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, StudentSchoolSession.class);
    }

    @Bean
    public OneWayCollectionMapper<StudentSchoolSession, OutboundStudentSchoolSession> studentSchoolSessionEntityToModelOneWayCollectionMapper(
            OneWayMapper<StudentSchoolSession, OutboundStudentSchoolSession> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * Student
     */

    @Bean
    public OneWayMapper<InboundStudent, Student> studentModelToEntityMapper(OneWayUpdateMapper<InboundStudent,
            Student> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, Student.class);
    }

    @Bean
    public OneWayMapper<InboundStudent, StudentSchoolSession> studentModelToSessionEntityMapper(
            OneWayUpdateMapper<InboundStudent, StudentSchoolSession> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, StudentSchoolSession.class);
    }

    /*
     * StudentActivityFocus
     */

    @Bean
    public CollectionSynchronizer<StudentActivityFocus> studentActivityFocusCollectionSyncHelper() {
        return new SimpleCollectionSynchronizer<>();
    }

    /*
     * StudentBehavior
     */

    @Bean
    public CollectionSynchronizer<StudentBehavior> studentBehaviorCollectionSyncHelper() {
        return new SimpleCollectionSynchronizer<>();
    }

    /*
     * StudentLeadershipSkill
     */

    @Bean
    public CollectionSynchronizer<StudentLeadershipSkill> studentLeadershipSkillCollectionSyncHelper() {
        return new SimpleCollectionSynchronizer<>();
    }

    /*
     * StudentLeadershipTrait
     */

    @Bean
    public CollectionSynchronizer<StudentLeadershipTrait> studentLeadershipTraitCollectionSyncHelper() {
        return new SimpleCollectionSynchronizer<>();
    }

    /*
     * StudentPerson
     */

    @Bean
    public CollectionSynchronizer<StudentPersonRole> studentPersonRoleCollectionSyncHelper() {
        return new SimpleCollectionSynchronizer<>();
    }

    @Bean
    public OneWayMapper<InboundContact, StudentPersonRole> studentPersonModelToEntityMapper(
            OneWayUpdateMapper<InboundContact, StudentPersonRole> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, StudentPersonRole.class);
    }

    @Bean
    public OneWayCollectionMapper<StudentPersonRole, OutboundContact> studentPersonEntityToModelOneWayCollectionMapper(
            OneWayMapper<StudentPersonRole, OutboundContact> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * Tag
     */

    @Bean
    public OneWayMapper<String, Tag> stringToTagMapper(TagService tagService) {
        return new FunctionBasedModelToEntityMapper<>(tagService::findTagByName);
    }

    @Bean
    public OneWayCollectionMapper<String, Tag> tagModelToEntityOneWayCollectionMapper(
            OneWayMapper<String, Tag> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    @Bean
    public OneWayCollectionMapper<Tag, String> tagEntityToModelOneWayCollectionMapper(
            OneWayMapper<Tag, String> mapper) {
        return new SimpleOneWayCollectionMapper<>(mapper);
    }

    /*
     * Teacher
     */

    @Bean
    public OneWayMapper<InboundTeacher, SchoolPersonRole> teacherModelToEntityMapper(OneWayUpdateMapper<InboundTeacher, SchoolPersonRole> mapper) {
        return new UpdateMapperBasedOneWayMapper<>(mapper, SchoolPersonRole.class);
    }

}
