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

import io.aiontechnology.mentorsuccess.model.Identifiable;
import io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation;
import io.aiontechnology.mentorsuccess.model.enumeration.RoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity that represents a person's role related to a student.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Entity
@Table(name = "school_person_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SchoolPersonRole implements Identifiable<UUID> {

    /** The ID of the role. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /** The school of the role. */
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    private School school;

    /** The person in the role. */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    /** The role type. */
    @Column
    @Enumerated(EnumType.STRING)
    private RoleType type;

    /** The lower grade boundary. (for teachers) */
    @Column
    private Integer grade1;

    /** The upper grade boundary. (for teachers) */
    @Column
    private Integer grade2;

    /** Availability. (for mentors) */
    @Column
    private String availability;

    /** Location. (for mentors) */
    @Column
    @Enumerated(EnumType.STRING)
    private ResourceLocation location;

    /** Media release. (for mentors) */
    @Column
    private Boolean isMediaReleaseSigned;

    /** Has a background check been completed. (for mentors) */
    @Column
    private Boolean isBackgroundCheckCompleted;

    /** Is the role active? */
    @Column
    private Boolean isActive;

    @Column
    private UUID idpUserId;

    public static class FirstNameComparator implements Comparator<SchoolPersonRole> {

        @Override
        public int compare(SchoolPersonRole role1, SchoolPersonRole role2) {
            return role1.getPerson().getFirstName().compareTo(role2.getPerson().getFirstName());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SchoolPersonRole that = (SchoolPersonRole) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
