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

import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.model.inbound.InboundProgramAdmin;
import io.aiontechnology.mentoring.util.PhoneService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ProgramAdminModelToEntityUpdateMapper}
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
public class ProgramAdminModelToEntityUpdateMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        String firstName = "FIRST_NAME";
        String lastName = "LAST_NAME";
        String workPhone = "WORK_PHONE";
        String cellPhone = "CELL_PHONE";
        String email = "EMAIL";

        InboundProgramAdmin inboundProgramAdmin = InboundProgramAdmin.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withWorkPhone(workPhone)
                .withCellPhone(cellPhone)
                .withEmail(email)
                .build();

        SchoolPersonRole schoolPersonRole = new SchoolPersonRole();

        ProgramAdminModelToEntityUpdateMapper programAdminModelToEntityUpdateMapper =
                new ProgramAdminModelToEntityUpdateMapper(new PhoneService());

        // execute the SUT
        Optional<SchoolPersonRole> result = programAdminModelToEntityUpdateMapper
                .map(Pair.of(inboundProgramAdmin, UUID.randomUUID()), schoolPersonRole);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get().getIsActive()).isTrue();
        assertThat(result.get().getPerson().getFirstName()).isEqualTo(firstName);
        assertThat(result.get().getPerson().getLastName()).isEqualTo(lastName);
        assertThat(result.get().getPerson().getWorkPhone()).isEqualTo(workPhone);
        assertThat(result.get().getPerson().getCellPhone()).isEqualTo(cellPhone);
        assertThat(result.get().getPerson().getEmail()).isEqualTo(email);
    }

}
