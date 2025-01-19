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

package io.aiontechnology.mentoring.api.mapping.toentity.resource;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.entity.reference.Phonogram;
import io.aiontechnology.mentoring.entity.reference.Tag;
import io.aiontechnology.mentoring.model.inbound.InboundBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * Mapper that updates a {@link Book} from a {@link InboundBook}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class BookModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundBook, Book> {

    /** Mapper between a behavior string and {@link Behavior}. */
    private final OneWayCollectionMapper<String, Behavior> behaviorModelToEntityMapper;

    /** Mapper between an interest string and {@link Interest}. */
    private final OneWayCollectionMapper<String, Interest> interestModelToEntityMapper;

    /** Mapper between a leadership skill string and {@link LeadershipSkill}. */
    private final OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper;

    /** Mapper between a leadership trait string and {@link LeadershipTrait}. */
    private final OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper;

    /** Mapper between a phonogram and {@link Phonogram}. */
    private final OneWayCollectionMapper<String, Phonogram> phonogramModelToEntityMapper;

    /** Mapper between a tag and {@link Tag}. */
    private final OneWayCollectionMapper<String, Tag> tagModelToEntityMapper;

    /**
     * Update the given {@link Book} with the given {@link InboundBook}.
     *
     * @param inboundBook The {@link InboundBook} to update from.
     * @param book The {@link Book} to update.
     * @return The updated {@link Book}.
     */
    @Override
    public Optional<Book> map(InboundBook inboundBook, Book book) {
        Objects.requireNonNull(book);
        return Optional.ofNullable(inboundBook)
                .map(b -> {
                    book.setTitle(b.getTitle());
                    book.setDescription(b.getDescription());
                    book.setAuthor(b.getAuthor());
                    book.setGradeLevel(b.getGradeLevel());
                    book.setLocation(b.getLocation());
                    book.setIsActive(true); // TODO Is this correct?
                    book.setBehaviors(behaviorModelToEntityMapper.map(b.getBehaviors()).orElse(Collections.emptyList()));
                    book.setInterests(interestModelToEntityMapper.map(b.getInterests()).orElse(Collections.emptyList()));
                    book.setLeadershipSkills(leadershipSkillModelToEntityMapper.map(b.getLeadershipSkills()).orElse(Collections.emptyList()));
                    book.setLeadershipTraits(leadershipTraitModelToEntityMapper.map(b.getLeadershipTraits()).orElse(Collections.emptyList()));
                    book.setPhonograms(phonogramModelToEntityMapper.map(b.getPhonograms()).orElse(Collections.emptyList()));
                    book.setTags(tagModelToEntityMapper.map(b.getTags()).orElse(Collections.emptyList()));
                    return book;
                });
    }

}
