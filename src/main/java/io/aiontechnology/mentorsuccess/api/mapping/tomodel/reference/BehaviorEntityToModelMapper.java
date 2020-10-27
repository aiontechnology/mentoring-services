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

package io.aiontechnology.mentorsuccess.api.mapping.tomodel.reference;

import io.aiontechnology.mentorsuccess.api.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.api.model.inbound.reference.BehaviorModel;
import io.aiontechnology.mentorsuccess.entity.reference.Behavior;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper that converts a {@link Behavior} to a {@link BehaviorModel}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
public class BehaviorEntityToModelMapper implements OneWayMapper<Behavior, BehaviorModel> {

    /**
     * Map the given {@link Behavior} to a {@link BehaviorModel}.
     *
     * @param behavior The {@link Behavior} to map.
     * @return The mapped {@link BehaviorModel}.
     */
    @Override
    public Optional<BehaviorModel> map(Behavior behavior) {
        return Optional.ofNullable(behavior)
                .map(b -> BehaviorModel.builder()
                        .withName(b.getName())
                        .build());
    }

}