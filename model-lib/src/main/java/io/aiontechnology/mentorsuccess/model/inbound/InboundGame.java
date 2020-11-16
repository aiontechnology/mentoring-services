/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentorsuccess.model.inbound;

import io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation;
import io.aiontechnology.mentorsuccess.model.validation.EnumNamePattern;
import io.aiontechnology.mentorsuccess.model.validation.GradeRange;
import io.aiontechnology.mentorsuccess.model.validation.GradeRangeHolder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.UUID;

/**
 * A model object for games.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Value
@Builder(setterPrefix = "with")
@GradeRange
public class InboundGame implements GradeRangeHolder {

    /** The game's id. */
    UUID id;

    /** The game's name. */
    @NotNull(message = "{game.name.notNull}")
    @Size(max = 40, message = "{game.name.size}")
    String name;

    /** The game's starting grade level. */
    @NotNull(message = "{game.grade1.notNull}")
    @Min(value = 1, message = "{game.grade1.invalid}")
    @Max(value = 6, message = "{game.grade1.invalid}")
    Integer grade1;

    /** The game's ending grade level. */
    @NotNull(message = "{game.grade2.notNull}")
    @Min(value = 1, message = "{game.grade2.invalid}")
    @Max(value = 6, message = "{game.grade2.invalid}")
    Integer grade2;

    @NotNull(message = "{game.location.notNull}")
    @EnumNamePattern(regexp = "ONLINE|OFFLINE|BOTH", message = "{book.location.invalid}")
    ResourceLocation location;

    /** The activity focus associated with the game. */
    Collection<String> activityFocuses;

    /** The leadership skills associated with the book. */
    Collection<String> leadershipSkills;

}
