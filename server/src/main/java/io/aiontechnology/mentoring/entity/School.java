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

package io.aiontechnology.mentoring.entity;

import io.aiontechnology.mentoring.model.Identifiable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity that represents a school.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Entity
@Table(name = "school")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Where(clause = "is_active = true")
@FilterDef(name = "roleType", parameters = @ParamDef(name = "type", type = String.class))
public class School implements Identifiable<UUID> {

    /** The ID of the school. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /** The name of the school. */
    @Column
    private String name;

    /** The street 1 of the school. */
    @Column
    private String street1;

    /** The street 2 of the school. */
    @Column
    private String street2;

    /** The city of the school. */
    @Column
    private String city;

    /** The state of the school. */
    @Column
    private String state;

    /** The zip code of the school. */
    @Column
    private String zip;

    /** The phone number of the school. */
    @Column
    private String phone;

    /** The district that the school is in. */
    @Column
    private String district;

    /** Is the school private? */
    @Column
    private Boolean isPrivate;

    /** Is the school active? */
    @Column
    private Boolean isActive;

    /** A tag that is appended to emails to mentorsuccess */
    @Column
    private String emailTag;

    /** The collection of {@link SchoolPersonRole Roles} associated with the school. */
    @ToString.Exclude
    @OneToMany(mappedBy = "school")
    @Where(clause = "is_active = true")
    @Filter(name = "roleType", condition = "type = :type")
    private Collection<SchoolPersonRole> roles;

    @ToString.Exclude
    @OneToMany(mappedBy = "school")
    private Collection<SchoolSession> sessions;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_session_id", referencedColumnName = "id")
    private SchoolSession currentSession;

    /** The collection of {@link Student Students} associated with the school. */
    @ToString.Exclude
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    @OrderBy("lastName")
    private Collection<Student> students;

    /** The collection of {@link Book books} associated with the school */
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "school_book",
            joinColumns = {@JoinColumn(name = "school_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id")}
    )
    private Collection<Book> books;

    /** The collection of {@link Game games} associated with the school */
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "school_game",
            joinColumns = {@JoinColumn(name = "school_id")},
            inverseJoinColumns = {@JoinColumn(name = "game_id")}
    )
    private Collection<Game> games;

    /**
     * Add a {@link SchoolPersonRole} to the school.
     *
     * @param role The {@link SchoolPersonRole} to add to the school.
     * @return The added {@link SchoolPersonRole}.
     */
    public SchoolPersonRole addRole(SchoolPersonRole role) {
        role.setSchool(this);
        roles.add(role);
        return role;
    }

    public Student addStudent(Student student) {
        students.add(student);
        student.setSchool(this);
        return student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        School school = (School) o;
        return id != null && Objects.equals(id, school.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
