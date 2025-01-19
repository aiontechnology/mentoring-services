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

package io.aiontechnology.mentoring.api.mapping.tomodel.student;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.StudentPersonRole;
import io.aiontechnology.mentoring.model.outbound.student.OutboundContact;
import io.aiontechnology.mentoring.util.PhoneService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class StudentPersonEntityToModelMapper implements OneWayMapper<StudentPersonRole, OutboundContact> {

    /** Mapper from {@link Person} to {@link URI}. */
//    @Qualifier("personAssemblerMapperAdaptor")
//    private final OneWayMapper<Person, OutboundPerson> personEntityToModelMapper;

    private final PhoneService phoneService;

    @Override
    public Optional<OutboundContact> map(StudentPersonRole studentPersonRole) {
        return Optional.ofNullable(studentPersonRole)
                .map(s -> OutboundContact.builder()
                        .withFirstName(s.getPerson().getFirstName())
                        .withLastName(s.getPerson().getLastName())
                        .withPhone(phoneService.format(s.getPerson().getCellPhone()))
                        .withEmail(s.getPerson().getEmail())
                        .withLabel(s.getLabel())
                        .withIsEmergencyContact(s.getIsEmergencyContact())
                        .withPreferredContactMethod(s.getPreferredContactMethod())
                        .withComment(s.getComment())
                        .build());
    }

}
