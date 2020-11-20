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

package io.aiontechnology.mentorsuccess.api.mapping.toentity.resource;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.mentorsuccess.entity.Book;
import io.aiontechnology.mentorsuccess.entity.reference.Behavior;
import io.aiontechnology.mentorsuccess.entity.reference.Interest;
import io.aiontechnology.mentorsuccess.entity.reference.LeadershipSkill;
import io.aiontechnology.mentorsuccess.entity.reference.LeadershipTrait;
import io.aiontechnology.mentorsuccess.entity.reference.Phonogram;
import io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation;
import io.aiontechnology.mentorsuccess.model.inbound.InboundBook;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation.ONLINE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BookModelToEntityUpdateMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class BookModelToEntityUpdateMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        UUID id = UUID.randomUUID();
        String title = "TITLE";
        String author = "AUTHOR";
        Integer gradeLevel = 1;
        ResourceLocation location = ONLINE;
        Collection<String> inboundBehaviors = Arrays.asList("BEHAVIOR");
        Collection<String> inboundInterests = Arrays.asList("INTEREST");
        Collection<String> inboundLeadershipSkills = Arrays.asList("LEADERSHIP_SKILL");
        Collection<String> inboundLeadershipTraits = Arrays.asList("LEADERSHIP_TRAIT");
        Collection<String> inboundPhonogramModels = Arrays.asList("PHONOGRAM");

        InboundBook inboundBook = InboundBook.builder()
                .withId(id)
                .withTitle(title)
                .withAuthor(author)
                .withGradeLevel(gradeLevel)
                .withLocation(location)
                .withBehaviors(inboundBehaviors)
                .withInterests(inboundInterests)
                .withLeadershipSkills(inboundLeadershipSkills)
                .withLeadershipTraits(inboundLeadershipTraits)
                .withPhonograms(inboundPhonogramModels)
                .build();

        Book book = new Book();

        OneWayCollectionMapper<String, Behavior> behaviorModelToEntityMapper =
                (b -> Arrays.asList(new Behavior()));
        OneWayCollectionMapper<String, Interest> interestModelToEntityMapper =
                (i -> Arrays.asList(new Interest()));
        OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper =
                (l -> Arrays.asList(new LeadershipSkill()));
        OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper =
                (l -> Arrays.asList(new LeadershipTrait()));
        OneWayCollectionMapper<String, Phonogram> phonogramModelToEntityMapper =
                (p -> Arrays.asList(new Phonogram()));

        BookModelToEntityUpdateMapper bookModelToEntityUpdateMapper = new BookModelToEntityUpdateMapper(
                behaviorModelToEntityMapper, interestModelToEntityMapper, leadershipSkillModelToEntityMapper,
                leadershipTraitModelToEntityMapper, phonogramModelToEntityMapper);

        // execute the SUT
        Optional<Book> result = bookModelToEntityUpdateMapper.map(inboundBook, book);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isSameAs(book);
        assertThat(result.get().getTitle()).isEqualTo(title);
        assertThat(result.get().getAuthor()).isEqualTo(author);
        assertThat(result.get().getGradeLevel()).isEqualTo(gradeLevel);
        assertThat(result.get().getLocation()).isEqualTo(location);
        assertThat(result.get().getBehaviors().size()).isEqualTo(1);
        assertThat(result.get().getInterests().size()).isEqualTo(1);
        assertThat(result.get().getLeadershipSkills().size()).isEqualTo(1);
        assertThat(result.get().getLeadershipTraits().size()).isEqualTo(1);
        assertThat(result.get().getPhonograms().size()).isEqualTo(1);
    }

    @Test
    void testNull() throws Exception {
        // setup the fixture
        Book book = new Book();

        Behavior behavior = new Behavior();
        OneWayCollectionMapper<String, Behavior> behaviorModelToEntityMapper =
                (b -> Arrays.asList(behavior));
        Interest interest = new Interest();
        OneWayCollectionMapper<String, Interest> interestModelToEntityMapper =
                (i -> Arrays.asList(interest));
        LeadershipSkill leadershipSkill = new LeadershipSkill();
        OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper =
                (l -> Arrays.asList(leadershipSkill));
        LeadershipTrait leadershipTrait = new LeadershipTrait();
        OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper =
                (l -> Arrays.asList(leadershipTrait));
        Phonogram phonogram = new Phonogram();
        OneWayCollectionMapper<String, Phonogram> phonogramModelToEntityMapper =
                (p -> Arrays.asList(phonogram));

        BookModelToEntityUpdateMapper bookModelToEntityUpdateMapper = new BookModelToEntityUpdateMapper(
                behaviorModelToEntityMapper, interestModelToEntityMapper, leadershipSkillModelToEntityMapper,
                leadershipTraitModelToEntityMapper, phonogramModelToEntityMapper);

        // execute the SUT
        Optional<Book> result = bookModelToEntityUpdateMapper.map(null, book);

        // validation
        assertThat(result).isEmpty();
    }

}
