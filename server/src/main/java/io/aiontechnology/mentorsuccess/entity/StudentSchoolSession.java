/*
 * Copyright 2021-2023 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.entity;

import io.aiontechnology.atlas.synchronization.CollectionSynchronizer;
import io.aiontechnology.atlas.synchronization.impl.SimpleCollectionSynchronizer;
import io.aiontechnology.mentorsuccess.entity.reference.ActivityFocus;
import io.aiontechnology.mentorsuccess.entity.reference.Interest;
import io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA Entity that represents a connection between a student and a school year.
 *
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Entity
@Table(name = "student_schoolsession")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentSchoolSession {

    private static final CollectionSynchronizer<Interest> interestSyncHelper =
            new SimpleCollectionSynchronizer<>();

    private static final CollectionSynchronizer<StudentActivityFocus> studentActivityFocusSyncHelper =
            new SimpleCollectionSynchronizer<>();

    private static final CollectionSynchronizer<StudentBehavior> studentBehaviorSyncHelper =
            new SimpleCollectionSynchronizer<>();

    private static final CollectionSynchronizer<StudentLeadershipSkill> studentLeadershipSkillSyncHelper =
            new SimpleCollectionSynchronizer<>();

    private static final CollectionSynchronizer<StudentLeadershipTrait> studentLeadershipTraitSyncHelper =
            new SimpleCollectionSynchronizer<>();

    @Column(name = "actual_time", length = 30)
    private String actualTime;

    /** The student's grade */
    @Column(name = "grade")
    private Integer grade;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_interest",
            joinColumns = {@JoinColumn(name = "studentsession_id")},
            inverseJoinColumns = {@JoinColumn(name = "interest_id")}
    )
    private Collection<Interest> interests = new ArrayList<>();

    @Column
    private Boolean isActive;

    @Column(name = "is_media_release_signed")
    private Boolean isMediaReleaseSigned;

    @Column(name = "is_registration_signed")
    private Boolean isRegistrationSigned;

    /** The location of the resource */
    @Column(name = "location", length = 20)
    @Enumerated(EnumType.STRING)
    private ResourceLocation location;

    @ManyToOne
    @JoinColumn(name = "mentor_role_id")
    private SchoolPersonRole mentor;

    @Column
    private Integer postBehavioralAssessment;

    @Column
    private Integer preBehavioralAssessment;

    @Column(name = "preferred_time", length = 30)
    private String preferredTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schoolsession_id", nullable = false)
    private SchoolSession schoolSession;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "pre_question1")
    private Integer preQuestion1;

    @Column(name = "pre_question2")
    private Integer preQuestion2;

    @Column(name = "pre_question3")
    private Integer preQuestion3;

    @Column(name = "pre_question4")
    private Integer preQuestion4;

    @Column(name = "pre_question5")
    private Integer preQuestion5;

    @Column(name = "pre_question6")
    private Integer preQuestion6;

    @Column(name = "pre_question7")
    private Integer preQuestion7;

    @Column(name = "pre_question8")
    private Integer preQuestion8;

    @Column(name = "pre_question9")
    private Integer preQuestion9;

    @Column(name = "pre_question10")
    private Integer preQuestion10;

    @Column(name = "pre_question11")
    private Integer preQuestion11;

    @Column(name = "pre_question12")
    private Integer preQuestion12;

    @Column(name = "pre_question13")
    private Integer preQuestion13;

    @Column(name = "pre_question14")
    private Integer preQuestion14;

    @Column(name = "pre_question15")
    private Integer preQuestion15;

    @Column(name = "pre_question16")
    private Integer preQuestion16;

    @Column(name = "pre_question17")
    private Integer preQuestion17;

    @Column(name = "pre_question18")
    private Integer preQuestion18;

    @Column(name = "pre_question19")
    private Integer preQuestion19;

    @Column(name = "pre_question20")
    private Integer preQuestion20;

    @Column(name = "pre_question21")
    private Integer preQuestion21;

    @Column(name = "pre_question22")
    private Integer preQuestion22;

    @Column(name = "pre_question23")
    private Integer preQuestion23;

    @Column(name = "pre_question24")
    private Integer preQuestion24;

    @Column(name = "pre_question25")
    private Integer preQuestion25;

    @Column(name = "pre_question26")
    private Integer preQuestion26;

    @Column(name = "pre_question27")
    private Integer preQuestion27;

    @Column(name = "pre_question28")
    private Integer preQuestion28;

    @Column(name = "pre_question29")
    private Integer preQuestion29;

    @Column(name = "pre_question30")
    private Integer preQuestion30;

    @Column(name = "pre_question31")
    private Integer preQuestion31;

    @Column(name = "pre_question32")
    private Integer preQuestion32;

    @Column(name = "pre_question33")
    private Integer preQuestion33;

    @Column(name = "pre_question34")
    private Integer preQuestion34;

    @Column(name = "pre_question35")
    private Integer preQuestion35;

    @Column(name = "post_question1")
    private Integer postQuestion1;

    @Column(name = "post_question2")
    private Integer postQuestion2;

    @Column(name = "post_question3")
    private Integer postQuestion3;

    @Column(name = "post_question4")
    private Integer postQuestion4;

    @Column(name = "post_question5")
    private Integer postQuestion5;

    @Column(name = "post_question6")
    private Integer postQuestion6;

    @Column(name = "post_question7")
    private Integer postQuestion7;

    @Column(name = "post_question8")
    private Integer postQuestion8;

    @Column(name = "post_question9")
    private Integer postQuestion9;

    @Column(name = "post_question10")
    private Integer postQuestion10;

    @Column(name = "post_question11")
    private Integer postQuestion11;

    @Column(name = "post_question12")
    private Integer postQuestion12;

    @Column(name = "post_question13")
    private Integer postQuestion13;

    @Column(name = "post_question14")
    private Integer postQuestion14;

    @Column(name = "post_question15")
    private Integer postQuestion15;

    @Column(name = "post_question16")
    private Integer postQuestion16;

    @Column(name = "post_question17")
    private Integer postQuestion17;

    @Column(name = "post_question18")
    private Integer postQuestion18;

    @Column(name = "post_question19")
    private Integer postQuestion19;

    @Column(name = "post_question20")
    private Integer postQuestion20;

    @Column(name = "post_question21")
    private Integer postQuestion21;

    @Column(name = "post_question22")
    private Integer postQuestion22;

    @Column(name = "post_question23")
    private Integer postQuestion23;

    @Column(name = "post_question24")
    private Integer postQuestion24;

    @Column(name = "post_question25")
    private Integer postQuestion25;

    @Column(name = "post_question26")
    private Integer postQuestion26;

    @Column(name = "post_question27")
    private Integer postQuestion27;

    @Column(name = "post_question28")
    private Integer postQuestion28;

    @Column(name = "post_question29")
    private Integer postQuestion29;

    @Column(name = "post_question30")
    private Integer postQuestion30;

    @Column(name = "post_question31")
    private Integer postQuestion31;

    @Column(name = "post_question32")
    private Integer postQuestion32;

    @Column(name = "post_question33")
    private Integer postQuestion33;

    @Column(name = "post_question34")
    private Integer postQuestion34;

    @Column(name = "post_question35")
    private Integer postQuestion35;

    @Column(name = "completed_info_flow_id")
    private String completedInfoFlowId;

    @Column(name = "started_info_flow_id")
    private String startedInfoFlowId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    /** The collection of {@link ActivityFocus ActivityFocuses} associated with the student. */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "studentSchoolSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentActivityFocus> studentActivityFocuses = new ArrayList<>();

    /** The collection of {@link StudentBehavior StudentBehaviors} associated with the student. */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "studentSchoolSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentBehavior> studentBehaviors = new ArrayList<>();

    /** The collection of {@link StudentLeadershipSkill StudentLeadershipSkills} associated with the student. */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "studentSchoolSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentLeadershipSkill> studentLeadershipSkills = new ArrayList<>();

    /** The collection of {@link StudentLeadershipTrait StudentLeadershipTraits} associated with the student. */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "studentSchoolSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentLeadershipTrait> studentLeadershipTraits = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacher_role_id")
    private SchoolPersonRole teacher;

    @Column(name = "teacher_comment")
    private String teacherComment;

    public StudentSchoolSession(StudentSchoolSession from) {
        grade = from.grade;
        location = from.location;
        isMediaReleaseSigned = from.isMediaReleaseSigned;
        student = from.student;
        isActive = from.isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentSchoolSession studentSchoolSession = (StudentSchoolSession) o;
        return id != null && Objects.equals(id, studentSchoolSession.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public StudentSchoolSession incrementGrade() {
        grade = grade + 1;
        return this;
    }

    public StudentSchoolSession setSession(SchoolSession session) {
        this.setSchoolSession(session);
        session.getStudentSchoolSessions().add(this);
        return this;
    }

    public void setStudentActivityFocuses(Collection<StudentActivityFocus> studentActivityFocuses) {
        Collection<StudentActivityFocus> newCollection = studentActivityFocuses.stream()
                .map(studentActivityFocus -> {
                    studentActivityFocus.getStudentActivityFocusPK().setStudentsession_id(getId());
                    studentActivityFocus.setStudentSchoolSession(this);
                    return studentActivityFocus;
                })
                .collect(Collectors.toList());
        this.studentActivityFocuses = studentActivityFocusSyncHelper.sync(this.studentActivityFocuses,
                newCollection);
    }

    public void setStudentBehaviors(Collection<StudentBehavior> studentBehaviors) {
        Collection<StudentBehavior> newCollection = studentBehaviors.stream()
                .map(studentBehavior -> {
                    studentBehavior.getStudentBehaviorPK().setStudentsession_id(getId());
                    studentBehavior.setStudentSchoolSession(this);
                    return studentBehavior;
                })
                .collect(Collectors.toList());
        this.studentBehaviors = studentBehaviorSyncHelper.sync(this.studentBehaviors, newCollection);
    }

    public void setStudentInterests(Collection<Interest> interests) {
        this.interests = interestSyncHelper.sync(this.interests, interests);
    }

    public void setStudentLeadershipSkills(Collection<StudentLeadershipSkill> studentLeadershipSkills) {
        Collection<StudentLeadershipSkill> newCollection = studentLeadershipSkills.stream()
                .map(studentLeadershipSkill -> {
                    studentLeadershipSkill.getStudentLeadershipSkillPK().setStudentsession_id(getId());
                    studentLeadershipSkill.setStudentSchoolSession(this);
                    return studentLeadershipSkill;
                })
                .collect(Collectors.toList());
        this.studentLeadershipSkills = studentLeadershipSkillSyncHelper.sync(this.studentLeadershipSkills,
                newCollection);
    }

    public void setStudentLeadershipTraits(Collection<StudentLeadershipTrait> studentLeadershipTraits) {
        Collection<StudentLeadershipTrait> newCollection = studentLeadershipTraits.stream()
                .map(studentLeadershipTrait -> {
                    studentLeadershipTrait.getStudentLeadershipTraitPK().setStudentsession_id(getId());
                    studentLeadershipTrait.setStudentSchoolSession(this);
                    return studentLeadershipTrait;
                })
                .collect(Collectors.toList());
        this.studentLeadershipTraits = studentLeadershipTraitSyncHelper.sync(this.studentLeadershipTraits,
                newCollection);
    }

}