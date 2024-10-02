/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.mapping.toentity.school;

import io.aiontechnology.mentoring.model.inbound.InboundSchoolSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class SchoolSessionModelToEntityUpdateMapperTest {

    @Test
    void testMapping() throws Exception {
        // setup the fixture
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        String label = "LABEL";

        InboundSchoolSession inboundSchoolSession = InboundSchoolSession.builder()
                .withLabel(label)
                .build();

        // execute the SUT

        // validation
    }

}
