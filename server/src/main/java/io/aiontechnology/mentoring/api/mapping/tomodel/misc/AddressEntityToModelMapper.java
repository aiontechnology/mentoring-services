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

package io.aiontechnology.mentoring.api.mapping.tomodel.misc;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.outbound.OutboundAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Mapper that converts a {@link OutboundAddress} to a {@link School}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class AddressEntityToModelMapper implements OneWayMapper<School, OutboundAddress> {

    @Override
    public Optional<OutboundAddress> map(School school) {
        return Optional.ofNullable(school)
                .map(s -> OutboundAddress.builder()
                        .withStreet1(school.getStreet1())
                        .withStreet2(school.getStreet2())
                        .withCity(school.getCity())
                        .withState(school.getState())
                        .withZip(school.getZip())
                        .build());
    }

}
