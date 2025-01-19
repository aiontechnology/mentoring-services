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

import io.aiontechnology.mentoring.model.enumeration.ContactMethod;
import io.aiontechnology.mentoring.model.enumeration.RoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * An entity that connects {@link Person Persons} to a {@link Student}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Entity
@Table(name = "student_person_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentPersonRole {

    @EmbeddedId
    private StudentPersonPK studentPersonPK = new StudentPersonPK();

    /** The type of the person associated with this entry */
    @Column
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private RoleType personType;

    @Column
    @EqualsAndHashCode.Include
    private Boolean isEmergencyContact;

    @Column
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private ContactMethod preferredContactMethod;

    @Column
    @EqualsAndHashCode.Include
    private String comment;

    @Column
    @EqualsAndHashCode.Include
    private String label;

    /** The associated {@link Student}. */
    @MapsId("student_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    /** The associated {@link Person}. */
    @MapsId("person_id")
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @EqualsAndHashCode.Include
    private Person person = new Person();

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class StudentPersonPK implements Serializable {

        private UUID student_id;
        private UUID person_id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentPersonRole that = (StudentPersonRole) o;
        return studentPersonPK != null && Objects.equals(studentPersonPK, that.studentPersonPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentPersonPK);
    }

}
