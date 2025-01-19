/*
 * Copyright 2020-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.model.outbound;

import io.aiontechnology.mentoring.model.BehaviorHolder;
import io.aiontechnology.mentoring.model.GradeHolder;
import io.aiontechnology.mentoring.model.Identifiable;
import io.aiontechnology.mentoring.model.LeadershipSkillHolder;
import io.aiontechnology.mentoring.model.LeadershipTraitHolder;
import io.aiontechnology.mentoring.model.LocationHolder;
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Whitney Hunter
 * @since 0.4.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OutboundBook<T> extends EntityModel<T> implements Identifiable<UUID>, LocationHolder,
        BehaviorHolder, LeadershipSkillHolder, LeadershipTraitHolder, GradeHolder {

    /** The book's id. */
    UUID id;

    /** The book's title. */
    String title;

    /** The book's description */
    String description;

    /** The book's author. */
    String author;

    /** The book's grade level. */
    Integer gradeLevel;

    /** The book's location */
    ResourceLocation location;

    /** The interests associated with the book. */
    Collection<String> interests;

    /** The leadership traits associated with the book. */
    Collection<String> leadershipTraits;

    /** The leadership skills associated with the book. */
    Collection<String> leadershipSkills;

    /** The phonograms associated with the book. */
    Collection<String> phonograms;

    /** The behaviors associated with the book. */
    Collection<String> behaviors;

    /** The tags associated with the book. */
    Collection<String> tags;

    public OutboundBook(T content) {
        super(content);
    }

}
