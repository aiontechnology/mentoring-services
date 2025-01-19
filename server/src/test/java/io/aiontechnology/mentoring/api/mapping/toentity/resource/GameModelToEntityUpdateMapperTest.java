/*
 * Copyright 2020-2023 Aion Technology LLC
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
import io.aiontechnology.mentoring.entity.Game;
import io.aiontechnology.mentoring.entity.reference.ActivityFocus;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import io.aiontechnology.mentoring.model.inbound.InboundGame;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentoring.model.enumeration.ResourceLocation.ONLINE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GameModelToEntityUpdateMapper}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class GameModelToEntityUpdateMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        UUID id = UUID.randomUUID();
        String name = "NAME";
        String description = "DESCRIPTION";
        Integer gradeLevel1 = 1;
        Integer gradeLevel2 = 2;
        ResourceLocation location = ONLINE;
        Collection<String> inboundActivityFoci = Arrays.asList("FOCUS");
        Collection<String> inboundLeadershipSkills = Arrays.asList("SKILL");

        InboundGame inboundGame = InboundGame.builder()
                .withId(id)
                .withName(name)
                .withDescription(description)
                .withGrade1(gradeLevel1)
                .withGrade2(gradeLevel2)
                .withLocation(location)
                .withActivityFocuses(inboundActivityFoci)
                .withLeadershipSkills(inboundLeadershipSkills)
                .build();

        Game game = new Game();

        OneWayCollectionMapper<String, ActivityFocus> activityFocusModelToEntityMapper =
                (b -> Optional.of(Arrays.asList(new ActivityFocus())));
        OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(new LeadershipSkill())));
        OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(new LeadershipTrait())));

        GameModelToEntityUpdateMapper gameModelToEntityUpdateMapper = new GameModelToEntityUpdateMapper(
                activityFocusModelToEntityMapper, leadershipSkillModelToEntityMapper,
                leadershipTraitModelToEntityMapper);

        // execute the SUT
        Optional<Game> result = gameModelToEntityUpdateMapper.map(inboundGame, game);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isSameAs(game);
        assertThat(result.get().getName()).isEqualTo(name);
        assertThat(result.get().getDescription()).isEqualTo(description);
        assertThat(result.get().getGrade1()).isEqualTo(gradeLevel1);
        assertThat(result.get().getGrade2()).isEqualTo(gradeLevel2);
        assertThat(result.get().getLocation()).isEqualTo(location);
        assertThat(result.get().getActivityFocuses().size()).isEqualTo(1);
        assertThat(result.get().getLeadershipSkills().size()).isEqualTo(1);
    }

    @Test
    void testNull() throws Exception {
        // setup the fixture
        Game game = new Game();

        OneWayCollectionMapper<String, ActivityFocus> activityFocusModelToEntityMapper =
                (b -> Optional.of(Arrays.asList(new ActivityFocus())));
        OneWayCollectionMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(new LeadershipSkill())));
        OneWayCollectionMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper =
                (l -> Optional.of(Arrays.asList(new LeadershipTrait())));

        GameModelToEntityUpdateMapper gameModelToEntityUpdateMapper = new GameModelToEntityUpdateMapper(
                activityFocusModelToEntityMapper, leadershipSkillModelToEntityMapper,
                leadershipTraitModelToEntityMapper);

        // execute the SUT
        Optional<Game> result = gameModelToEntityUpdateMapper.map(null, game);

        // validation
        assertThat(result).isEmpty();
    }

}
