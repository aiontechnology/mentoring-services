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

import io.aiontechnology.mentorsuccess.entity.reference.Interest;
import io.aiontechnology.mentorsuccess.entity.reference.LeadershipSkill;
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
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * An entity that connects {@link Interest LeadershipSkills} to a {@link Student}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Entity
@Table(name = "student_interest")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class StudentInterest {

    @EmbeddedId
    private StudentInterestPK studentInterestPK = new StudentInterestPK();

    /** The associated {@link Student}. */
    @MapsId("student_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "studentsession_id", referencedColumnName = "id")
    private Student student;

    /** The associated {@link LeadershipSkill}. */
    @MapsId("interest_id")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "interest_id", referencedColumnName = "id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Interest interest;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class StudentInterestPK implements Serializable {

        private UUID student_id;
        private UUID interest_id;
        private UUID role_id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentInterest that = (StudentInterest) o;
        return studentInterestPK != null && Objects.equals(studentInterestPK, that.studentInterestPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentInterestPK);
    }

}
