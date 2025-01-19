/*
 * Copyright 2021-2022 Aion Technology LLC
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@Component
@Slf4j
public class SystemAdminAuthoritySetter implements BiFunction<Optional<SchoolPersonRole>, URI, List<GrantedAuthority>> {

    private List<GrantedAuthority> authorities;

    @Override
    public List<GrantedAuthority> apply(Optional<SchoolPersonRole> role, URI requestUri) {
        if (authorities == null) {
            authorities = new ArrayList<>();
            authorities.addAll(AuthoritiesBuilder.instance("book").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("game").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("mentor").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("person").withRead().withCreate().build());
            authorities.addAll(AuthoritiesBuilder.instance("personnel").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("program-admin").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("resource").withRead().build());
            authorities.addAll(AuthoritiesBuilder.instance("resources").withUpdate().build());
            authorities.addAll(AuthoritiesBuilder.instance("school").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("schoolresources").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("schoolsession").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("schoolsessionstudents").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("student").withAll().build());
            authorities.addAll(AuthoritiesBuilder.instance("teacher").withAll().build());
        }
        log.debug("==> Authorities: {}", authorities);
        return authorities;
    }

}
