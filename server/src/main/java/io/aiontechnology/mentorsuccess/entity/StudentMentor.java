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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
 * An entity that connects mentor to a {@link Student}.
 *
 * @author Whitney Hunter
 * @since 0.6.0
 */
@Entity
@Table(name = "student_mentor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentMentor {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private StudentMentorPK studentMentorPK = new StudentMentorPK();

    /** The associated {@link Student}. */
    @MapsId("student_id")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "studentsession_id", referencedColumnName = "id")
    private StudentSchoolSession studentSchoolSession;

    /** The associated {@link SchoolPersonRole}. */
    @MapsId("role_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private SchoolPersonRole role;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class StudentMentorPK implements Serializable {

        private UUID student_id;
        private UUID role_id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentMentor that = (StudentMentor) o;
        return studentMentorPK != null && Objects.equals(studentMentorPK, that.studentMentorPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentMentorPK);
    }

}
