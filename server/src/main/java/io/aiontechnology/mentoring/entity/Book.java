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

import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.entity.reference.Phonogram;
import io.aiontechnology.mentoring.entity.reference.Tag;
import io.aiontechnology.mentoring.model.Identifiable;
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity that represents a book.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Where(clause = "is_active = true")
public class Book implements Identifiable<UUID> {

    /** The author of the book. */
    @Column
    private String author;

    /** A collection behaviors for the book. */
    @ManyToMany
    @JoinTable(name = "book_behavior",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "behavior_id"))
    @ToString.Exclude
    private Collection<Behavior> behaviors = new ArrayList<>();

    @Column
    private String description;

    /** The grade level of the book. */
    @Column
    private Integer gradeLevel;

    /** The ID of the book. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    /** A collection of interests related to the book. */
    @ManyToMany
    @JoinTable(name = "book_interest",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    @ToString.Exclude
    private Collection<Interest> interests = new ArrayList<>();

    /** Is the book active? */
    @Column
    private Boolean isActive;

    /** A collection leadership skills for the book. */
    @ManyToMany
    @JoinTable(name = "book_leadershipskill",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "leadershipskill_id"))
    @ToString.Exclude
    private Collection<LeadershipSkill> leadershipSkills = new ArrayList<>();

    /** A collection leadership traits for the book. */
    @ManyToMany
    @JoinTable(name = "book_leadershiptrait",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "leadershiptrait_id"))
    @ToString.Exclude
    private Collection<LeadershipTrait> leadershipTraits = new ArrayList<>();

    /** The location of the resource */
    @Column
    @Enumerated(EnumType.STRING)
    private ResourceLocation location;

    /** A collection phonograms for the book. */
    @ManyToMany
    @JoinTable(name = "book_phonogram",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "phonogram_id"))
    @ToString.Exclude
    private Collection<Phonogram> phonograms = new ArrayList<>();

    /** A collection tags for the book. */
    @ManyToMany
    @JoinTable(name = "book_tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ToString.Exclude
    private Collection<Tag> tags = new ArrayList<>();

    /** The title of the book. */
    @Column
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return id != null && Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
