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

package io.aiontechnology.mentoring.api.mapping.toentity.person;

import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.model.inbound.InboundMentor;
import io.aiontechnology.mentoring.util.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static io.aiontechnology.mentoring.model.enumeration.RoleType.MENTOR;

/**
 * Mapper from {@link InboundMentor} to {@link SchoolPersonRole}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class MentorModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundMentor, SchoolPersonRole> {

    /** Phone number formatting service */
    private final PhoneService phoneService;

    @Override
    public Optional<SchoolPersonRole> map(InboundMentor mentorModel, SchoolPersonRole role) {
        Objects.requireNonNull(role);
        return Optional.ofNullable(mentorModel)
                .map(m -> {
                    Person person = role.getPerson() != null ? role.getPerson() : new Person();
                    person.setFirstName(m.getFirstName());
                    person.setLastName(m.getLastName());
                    person.setEmail(m.getEmail());
                    person.setWorkPhone(phoneService.normalize(m.getWorkPhone()));
                    person.setCellPhone(phoneService.normalize(m.getCellPhone()));

                    role.setPerson(person);
                    role.setAvailability(m.getAvailability());
                    role.setLocation(m.getLocation());
                    role.setIsMediaReleaseSigned(m.getMediaReleaseSigned());
                    role.setIsBackgroundCheckCompleted(m.getBackgroundCheckCompleted());
                    role.setIsActive(true);
                    role.setType(MENTOR);
                    return role;
                });
    }

}
