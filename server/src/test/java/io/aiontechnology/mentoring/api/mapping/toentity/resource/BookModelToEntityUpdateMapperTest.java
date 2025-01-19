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
import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.entity.reference.Phonogram;
import io.aiontechnology.mentoring.entity.reference.Tag;
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import io.aiontechnology.mentoring.model.inbound.InboundBook;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentoring.model.enumeration.ResourceLocation.ONLINE;
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
                (b -> Optional.of(Arrays.asList(new Behavior())));
        OneWayCollectionMapper<String, Interest> interestModelToEntityMapper =
                (i -> Optional.of(Arrays.asList(new Interest())));
        OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(new LeadershipSkill())));
        OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(new LeadershipTrait())));
        OneWayCollectionMapper<String, Phonogram> phonogramModelToEntityMapper =
                (p -> Optional.of(Arrays.asList(new Phonogram())));
        OneWayCollectionMapper<String, Tag> tagModelToEntityMapper =
                (p -> Optional.of(Arrays.asList(new Tag())));

        BookModelToEntityUpdateMapper bookModelToEntityUpdateMapper = new BookModelToEntityUpdateMapper(
                behaviorModelToEntityMapper, interestModelToEntityMapper, leadershipSkillModelToEntityMapper,
                leadershipTraitModelToEntityMapper, phonogramModelToEntityMapper, tagModelToEntityMapper);

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
                (b -> Optional.of(Arrays.asList(behavior)));
        Interest interest = new Interest();
        OneWayCollectionMapper<String, Interest> interestModelToEntityMapper =
                (i -> Optional.of(Arrays.asList(interest)));
        LeadershipSkill leadershipSkill = new LeadershipSkill();
        OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(leadershipSkill)));
        LeadershipTrait leadershipTrait = new LeadershipTrait();
        OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(leadershipTrait)));
        Phonogram phonogram = new Phonogram();
        OneWayCollectionMapper<String, Phonogram> phonogramModelToEntityMapper =
                (p -> Optional.of(Arrays.asList(phonogram)));
        OneWayCollectionMapper<String, Tag> tagModelToEntityMapper =
                (p -> Optional.of(Arrays.asList(new Tag())));

        BookModelToEntityUpdateMapper bookModelToEntityUpdateMapper = new BookModelToEntityUpdateMapper(
                behaviorModelToEntityMapper, interestModelToEntityMapper, leadershipSkillModelToEntityMapper,
                leadershipTraitModelToEntityMapper, phonogramModelToEntityMapper, tagModelToEntityMapper);

        // execute the SUT
        Optional<Book> result = bookModelToEntityUpdateMapper.map(null, book);

        // validation
        assertThat(result).isEmpty();
    }

}
