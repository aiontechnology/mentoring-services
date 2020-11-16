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

package io.aiontechnology.mentorsuccess.api.mapping.toentity.reference;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.entity.reference.LeadershipTrait;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * Mapper that converts a leadership trait string to a {@link LeadershipTrait}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class LeadershipTraitModelToEntityMapper implements OneWayMapper<String, LeadershipTrait> {

    /** Function that retrieves a {@link LeadershipTrait} by its name */
    private final Function<String, Optional<LeadershipTrait>> getter;

    /**
     * Map the given leadership trait string.
     *
     * @param inboundLeadershipTrait The leadership trait string to map.
     * @return The resulting {@link LeadershipTrait}.
     */
    @Override
    public Optional<LeadershipTrait> map(String inboundLeadershipTrait) {
        return Optional.ofNullable(inboundLeadershipTrait)
                .flatMap(getter::apply);
    }

}
