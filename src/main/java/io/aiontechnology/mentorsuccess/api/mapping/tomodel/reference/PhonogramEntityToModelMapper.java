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
import io.aiontechnology.mentorsuccess.api.model.inbound.PhonogramModel;
import io.aiontechnology.mentorsuccess.entity.reference.Phonogram;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper that converts a {@link Phonogram} to a {@link PhonogramModel}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
public class PhonogramEntityToModelMapper implements OneWayMapper<Phonogram, PhonogramModel> {

    /**
     * Map the given {@link Phonogram} to a {@link PhonogramModel}.
     *
     * @param phonogram The {@link Phonogram} to map.
     * @return The mapped {@link PhonogramModel}.
     */
    @Override
    public Optional<PhonogramModel> map(Phonogram phonogram) {
        return Optional.ofNullable(phonogram)
                .map(p -> PhonogramModel.builder()
                        .withName(phonogram.getName())
                        .build());
    }

}