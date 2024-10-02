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

import io.aiontechnology.mentoring.model.GradeRangeHolder;
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
public class OutboundGame<T> extends EntityModel<T> implements Identifiable<UUID>, LocationHolder,
        LeadershipSkillHolder, LeadershipTraitHolder, GradeRangeHolder {

    /** The game's id. */
    UUID id;

    /** The game's name. */
    String name;

    /** The game's description */
    String description;

    /** The game's starting grade level. */
    Integer grade1;

    /** The game's ending grade level. */
    Integer grade2;

    ResourceLocation location;

    /** The activity focus associated with the game. */
    Collection<String> activityFocuses;

    /** The leadership skills associated with the game. */
    Collection<String> leadershipSkills;

    /** The leadership traits associated with the game. */
    Collection<String> leadershipTraits;

    public OutboundGame(T content) {
        super(content);
    }

}
