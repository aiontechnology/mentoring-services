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

package io.aiontechnology.mentoring.api.mapping.toentity.person;

import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.model.inbound.InboundPerson;
import io.aiontechnology.mentoring.util.PhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Mapper that updates a {@link Person} from a {@link InboundPerson}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class PersonModelToEntityUpdateMapper implements OneWayUpdateMapper<InboundPerson, Person> {

    private final PhoneService phoneService;

    /**
     * Map the given {@link InboundPerson} to the given {@link Person}.
     *
     * @param inboundPerson The {@link InboundPerson} to map.
     * @param person The {@link Person} to map to.
     * @return The resulting {@link Person}.
     */
    @Override
    public Optional<Person> map(InboundPerson inboundPerson, Person person) {
        Objects.requireNonNull(person);
        return Optional.ofNullable(inboundPerson)
                .map(p -> {
                    person.setFirstName(p.getFirstName());
                    person.setLastName(p.getLastName());
                    person.setWorkPhone(phoneService.normalize(p.getWorkPhone()));
                    person.setCellPhone(phoneService.normalize(p.getCellPhone()));
                    person.setEmail(p.getEmail());
                    return person;
                });
    }

}
