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

package io.aiontechnology.mentoring.model.inbound;

import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.UUID;

/**
 * A model object for books.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundBook {

    /** The book's id. */
    UUID id;

    /** The book's title. */
    @NotNull(message = "{book.title.notNull}")
    @Size(max = 100, message = "{book.title.size}")
    String title;

    @Size(max = 255, message = "{book.description.size")
    String description;

    /** The book's author. */
    @Size(max = 30, message = "{book.author.size}")
    String author;

    /** The book's grade level. */
    @NotNull(message = "{book.gradeLevel.notNull}")
    @Min(value = 1, message = "{book.gradeLevel.invalid}")
    @Max(value = 12, message = "{book.gradeLevel.invalid}")
    Integer gradeLevel;

    @NotNull(message = "{book.location.notNull}")
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

    /** The tag associated with the book. */
    Collection<String> tags;

}
