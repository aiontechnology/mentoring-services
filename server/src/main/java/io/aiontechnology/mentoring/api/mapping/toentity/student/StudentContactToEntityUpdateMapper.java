/*
 * Copyright 2020-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayCollectionUpdateMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.synchronization.CollectionSynchronizer;
import io.aiontechnology.mentoring.entity.StudentPersonRole;
import io.aiontechnology.mentoring.model.inbound.student.InboundContact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class StudentContactToEntityUpdateMapper implements OneWayCollectionUpdateMapper<InboundContact, StudentPersonRole> {

    // Mappers
    private final OneWayMapper<InboundContact, StudentPersonRole> contactModelToEntityMapper;

    // Helpers
    private final CollectionSynchronizer<StudentPersonRole> syncHelper;

    @Override
    public Collection<StudentPersonRole> map(Collection<InboundContact> inboundContacts,
            Collection<StudentPersonRole> studentPersonRoles) {
        if (inboundContacts == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<StudentPersonRole> newCollection = inboundContacts.stream()
                .map(i -> contactModelToEntityMapper.map(i).orElse(null))
                .collect(Collectors.toList());
        return syncHelper.sync(studentPersonRoles, newCollection);
    }

}
