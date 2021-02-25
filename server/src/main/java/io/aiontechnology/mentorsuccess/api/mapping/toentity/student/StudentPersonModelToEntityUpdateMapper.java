/*
 * Copyright 2020-2021 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentorsuccess.entity.Person;
import io.aiontechnology.mentorsuccess.entity.StudentPersonRole;
import io.aiontechnology.mentorsuccess.model.inbound.InboundPerson;
import io.aiontechnology.mentorsuccess.model.inbound.student.InboundContact;
import io.aiontechnology.mentorsuccess.util.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.aiontechnology.mentorsuccess.model.enumeration.RoleType.CONTACT;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class StudentPersonModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundContact, StudentPersonRole> {

    private final OneWayUpdateMapper<InboundPerson, Person> personModelToEntityMapper;

    private final PhoneService phoneService;

    @Override
    public Optional<StudentPersonRole> map(InboundContact inboundContact, StudentPersonRole studentPersonRole) {
        return Optional.ofNullable(inboundContact)
                .map(contactModel -> {
                    InboundPerson inboundPerson = InboundPerson.builder()
                            .withFirstName(contactModel.getFirstName())
                            .withLastName(contactModel.getLastName())
                            .withCellPhone(phoneService.normalize(contactModel.getPhone()))
                            .withEmail(contactModel.getEmail())
                            .build();
                    studentPersonRole.setPersonType(CONTACT);
                    studentPersonRole.setPerson(personModelToEntityMapper.map(inboundPerson, studentPersonRole.getPerson())
                            .orElse(null));
                    studentPersonRole.setIsEmergencyContact(contactModel.getIsEmergencyContact());
                    studentPersonRole.setPreferredContactMethod(contactModel.getPreferredContactMethod());
                    studentPersonRole.setLabel(inboundContact.getLabel());
                    studentPersonRole.setComment(contactModel.getComment());
                    return studentPersonRole;
                });
    }

}
