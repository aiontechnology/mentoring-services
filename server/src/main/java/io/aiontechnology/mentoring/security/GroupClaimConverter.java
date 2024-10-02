/*
 * Copyright 2021 Aion Technology LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.aiontechnology.mentoring.security;

import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GroupClaimConverter implements
        Converter<Object, BiFunction<Optional<SchoolPersonRole>, URI, List<GrantedAuthority>>> {

    private final String SYSTEM_ADMIN_GROUP = "SYSTEM_ADMIN";
    private final String PROGRAM_ADMIN_GROUP = "PROGRAM_ADMIN";

    private final SystemAdminAuthoritySetter systemAdminAuthoritySetter;
    private final ProgramAdminAuthoritySetter programAdminAuthoritySetter;

    @Override
    public BiFunction<Optional<SchoolPersonRole>, URI, List<GrantedAuthority>> convert(Object groups) {
        List groupNames = (List) groups;
        log.debug("==> GROUPS FOUND: {}", groupNames);
        if (groupNames.size() > 0) {
            return switch ((String) groupNames.get(0)) {
                case SYSTEM_ADMIN_GROUP -> systemAdminAuthoritySetter;
                case PROGRAM_ADMIN_GROUP -> programAdminAuthoritySetter;
                default -> (role, uri) -> Collections.emptyList();
            };
        }
        return (role, uri) -> Collections.emptyList();
    }

}
