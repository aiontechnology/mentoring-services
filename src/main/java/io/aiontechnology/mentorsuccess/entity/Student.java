/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentorsuccess.entity;

import io.aiontechnology.mentorsuccess.api.mapping.CollectionSyncHelper;
import io.aiontechnology.mentorsuccess.entity.reference.Interest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

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
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entity that represents a student.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Where(clause = "is_active = true")
public class Student {

    private static final CollectionSyncHelper<Interest> interestSyncHelper =
            new CollectionSyncHelper<>();

    private static final CollectionSyncHelper<StudentBehavior> studentBehaviorSyncHelper =
            new CollectionSyncHelper<>();

    private static final CollectionSyncHelper<StudentLeadershipSkill> studentLeadershipSkillSyncHelper =
            new CollectionSyncHelper<>();

    private static final CollectionSyncHelper<StudentLeadershipTrait> studentLeadershipTraitSyncHelper =
            new CollectionSyncHelper<>();

    /** The ID of the student. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /** The student's id given by the school */
    @Column
    private String studentId;

    /** The student's first name */
    @Column
    private String firstName;

    /** The student's last name */
    @Column
    private String lastName;

    /** The student's grade */
    @Column
    private Integer grade;

    @Column
    private String preferredTime;

    /** The location of the resource */
    @Column
    @Enumerated(EnumType.STRING)
    private ResourceLocation location;

    @Column
    private Boolean isMediaReleaseSigned;

    @Column
    private String allergyInfo;

    /** Is the student active? */
    @Column
    private Boolean isActive;

    /** The school which the student attends. */
    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    private School school;

    /** The student's teacher. */
    @ToString.Exclude
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private StudentStaff teacher;

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_interest",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "interest_id")}
    )
    private Collection<Interest> interests = new ArrayList<>();

    /** The collection of {@link StudentBehavior StudentBehaviors} associated with the student. */
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentBehavior> studentBehaviors = new ArrayList<>();

    /** The collection of {@link StudentLeadershipSkill StudentLeadershipSkills} associated with the student. */
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentLeadershipSkill> studentLeadershipSkills = new ArrayList<>();

    /** The collection of {@link StudentLeadershipTrait StudentLeadershipTraits} associated with the student. */
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentLeadershipTrait> studentLeadershipTraits = new ArrayList<>();

    /** The collection of {@link Person Persons} associated with the student. */
    @ToString.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Collection<StudentPersonRole> studentPersonRoles;

    public void setStudentInterests(Collection<Interest> interests) {
        this.interests = interestSyncHelper.sync(this.interests, interests);
    }

    public void setStudentBehaviors(Collection<StudentBehavior> studentBehaviors) {
        Collection<StudentBehavior> newCollection = studentBehaviors.stream()
                .map(studentBehavior -> {
                    studentBehavior.getStudentBehaviorPK().setStudent_id(getId());
                    studentBehavior.setStudent(this);
                    return studentBehavior;
                })
                .collect(Collectors.toList());
        this.studentBehaviors = studentBehaviorSyncHelper.sync(this.studentBehaviors, newCollection);
    }

    public void setStudentLeadershipSkills(Collection<StudentLeadershipSkill> studentLeadershipSkills) {
        Collection<StudentLeadershipSkill> newCollection = studentLeadershipSkills.stream()
                .map(studentLeadershipSkill -> {
                    studentLeadershipSkill.getStudentLeadershipSkillPK().setStudent_id(getId());
                    studentLeadershipSkill.setStudent(this);
                    return studentLeadershipSkill;
                })
                .collect(Collectors.toList());
        this.studentLeadershipSkills = studentLeadershipSkillSyncHelper.sync(this.studentLeadershipSkills, newCollection);
    }

    public void setStudentLeadershipTraits(Collection<StudentLeadershipTrait> studentLeadershipTraits) {
        Collection<StudentLeadershipTrait> newCollection = studentLeadershipTraits.stream()
                .map(studentLeadershipTrait -> {
                    studentLeadershipTrait.getStudentLeadershipTraitPK().setStudent_id(getId());
                    studentLeadershipTrait.setStudent(this);
                    return studentLeadershipTrait;
                })
                .collect(Collectors.toList());
        this.studentLeadershipTraits = studentLeadershipTraitSyncHelper.sync(this.studentLeadershipTraits, newCollection);
    }

    public void setStudentPersonRoles(Collection<StudentPersonRole> studentPersonRoles) {
        this.studentPersonRoles = studentPersonRoles.stream()
                .map(studentPerson -> {
                    studentPerson.getStudentPersonPK().setStudent_id(getId());
                    studentPerson.setStudent(this);
                    return studentPerson;
                })
                .collect(Collectors.toList());
    }

    public void setTeacher(StudentStaff teacher) {
        teacher.setStudent(this);
        this.teacher = teacher;
    }

}