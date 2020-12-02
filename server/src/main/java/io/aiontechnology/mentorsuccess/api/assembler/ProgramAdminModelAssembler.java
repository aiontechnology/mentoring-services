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

package io.aiontechnology.mentorsuccess.api.assembler;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.model.inbound.InboundProgramAdmin;
import io.aiontechnology.mentorsuccess.model.outbound.OutboundProgramAdmin;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Assembles a {@link OutboundProgramAdmin} from a {@link SchoolPersonRole}.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Component
public class ProgramAdminModelAssembler extends BaseRoleModelAssembler<OutboundProgramAdmin> {

    /**
     * Constructor
     *
     * @param mapper The mapper for mapping between {@link SchoolPersonRole} and {@link OutboundProgramAdmin}.
     * @param linkHelper A utility class for adding links to a model object.
     */
    @Inject
    public ProgramAdminModelAssembler(OneWayMapper<SchoolPersonRole, OutboundProgramAdmin> mapper,
            LinkHelper<OutboundProgramAdmin> linkHelper) {
        super(InboundProgramAdmin.class, OutboundProgramAdmin.class, mapper, linkHelper);
    }

}