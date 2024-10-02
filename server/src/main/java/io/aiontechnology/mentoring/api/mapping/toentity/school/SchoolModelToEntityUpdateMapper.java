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

package io.aiontechnology.mentoring.api.mapping.toentity.school;

import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.model.inbound.InboundAddress;
import io.aiontechnology.mentoring.model.inbound.InboundSchool;
import io.aiontechnology.mentoring.util.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Mapper that updates a {@link School} from a {@link InboundSchool}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class SchoolModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundSchool, School> {

    /** An update mapper between {@link InboundAddress} and {@link School}. */
    private final OneWayUpdateMapper<InboundAddress, School> addressMapper;

    /** A service for formatting phone numbers */
    private final PhoneService phoneService;

    /**
     * Update the given {@link School} with the given {@link InboundSchool}.
     *
     * @param inboundSchool The {@link InboundSchool} to update from.
     * @param school The {@link School} to update.
     * @return The updated {@link School}.
     */
    @Override
    public Optional<School> map(InboundSchool inboundSchool, School school) {
        Objects.requireNonNull(school);
        return Optional.ofNullable(inboundSchool)
                .map(s -> {
                    school.setName(inboundSchool.getName());
                    school.setPhone(phoneService.normalize(inboundSchool.getPhone()));
                    school.setDistrict(inboundSchool.getDistrict());
                    school.setIsPrivate(inboundSchool.getIsPrivate());
                    school.setIsActive(true);
                    school.setEmailTag(inboundSchool.getEmailTag());
                    addressMapper.map(inboundSchool.getAddress(), school);
                    return school;
                });
    }

}
