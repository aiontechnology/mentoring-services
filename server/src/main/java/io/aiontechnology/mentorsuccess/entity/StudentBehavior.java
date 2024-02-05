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

import io.aiontechnology.mentorsuccess.entity.reference.Behavior;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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

import java.io.Serializable;
import java.util.UUID;

/**
 * An entity that connects {@link Behavior Behaviors} to a {@link Student}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Entity
@Table(name = "student_behavior")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StudentBehavior {

    /** The associated {@link Behavior}. */
    @MapsId("behavior_id")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "behavior_id", referencedColumnName = "id", insertable = false, updatable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Behavior behavior = new Behavior();

    /** The associated {@link SchoolPersonRole}. */
    @MapsId("role_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @EqualsAndHashCode.Include
    private SchoolPersonRole role;

    @EmbeddedId
    private StudentBehaviorPK studentBehaviorPK = new StudentBehaviorPK();

    /** The associated {@link StudentSchoolSession}. */
    @MapsId("studentsession_id")
    @ManyToOne
    @JoinColumn(name = "studentsession_id", referencedColumnName = "id")
    private StudentSchoolSession studentSchoolSession;

    public StudentBehavior(StudentBehavior from) {
        behavior = from.behavior;
        role = from.role;
        studentSchoolSession = from.studentSchoolSession;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class StudentBehaviorPK implements Serializable {

        private UUID behavior_id;
        private UUID role_id;
        private UUID studentsession_id;

    }

}
