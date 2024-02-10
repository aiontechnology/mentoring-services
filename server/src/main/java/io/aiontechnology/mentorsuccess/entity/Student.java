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

package io.aiontechnology.mentorsuccess.entity;

import io.aiontechnology.atlas.synchronization.CollectionSynchronizer;
import io.aiontechnology.atlas.synchronization.impl.SimpleCollectionSynchronizer;
import io.aiontechnology.mentorsuccess.model.Identifiable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Entity that represents a student.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Student implements Identifiable<UUID> {

    private static final CollectionSynchronizer<StudentPersonRole> studentPersonRoleSyncHelper =
            new SimpleCollectionSynchronizer<>();

    /** The student's first name */
    @Column(nullable = false)
    private String firstName;

    /** The ID of the student. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /** The student's last name */
    @Column(nullable = false)
    private String lastName;

    /** The school which the student attends. */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    private School school;

    /** The student's id given by the school */
    @Column
    private String studentId;

    /** The collection of {@link Person Persons} associated with the student. */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentPersonRole> studentPersonRoles = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StudentSchoolSession> studentSchoolSessions = new ArrayList<>();

    /**
     * Add a new session to the student.
     *
     * @param studentSchoolSession The session to add
     * @return This student.
     */
    public Student addStudentSession(StudentSchoolSession studentSchoolSession) {
        studentSchoolSessions.add(studentSchoolSession);
        studentSchoolSession.setStudent(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Student student = (Student) o;
        return id != null && Objects.equals(id, student.id);
    }

    public Optional<StudentSchoolSession> findCurrentSessionForStudent(SchoolSession currentSession) {
        requireNonNull(currentSession);
        return getStudentSchoolSessions().stream()
                .filter(s -> s.getSchoolSession().getId().equals(currentSession.getId()))
                .findFirst();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public void setStudentPersonRoles(Collection<StudentPersonRole> studentPersonRoles) {
        Collection<StudentPersonRole> newCollection = studentPersonRoles.stream().peek(studentPerson -> {
            studentPerson.getStudentPersonPK().setStudent_id(getId());
            studentPerson.setStudent(this);
        }).collect(Collectors.toList());
        this.studentPersonRoles = studentPersonRoleSyncHelper.sync(this.studentPersonRoles, newCollection);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
