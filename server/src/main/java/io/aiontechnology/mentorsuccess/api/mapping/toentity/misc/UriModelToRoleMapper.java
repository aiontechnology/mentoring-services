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

package io.aiontechnology.mentorsuccess.api.mapping.toentity.misc;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class UriModelToRoleMapper implements OneWayMapper<URI, SchoolPersonRole> {

    private final Pattern TEACHER_PATTERN =
            Pattern.compile("https?://.*/api/v1/schools/(.*)/teachers/(.*)");

    private final RoleService roleService;

    @Override
    public Optional<SchoolPersonRole> map(URI uri) {
        return Optional.ofNullable(uri).
                flatMap(u -> {
                    UUID roleUUID = null;

                    Matcher matcher2 = TEACHER_PATTERN.matcher(uri.toString());
                    if (matcher2.find()) {
                        roleUUID = UUID.fromString(matcher2.group(2));
                    }

                    return roleUUID != null ? roleService.findRoleById(roleUUID) : Optional.empty();
                });
    }

}
