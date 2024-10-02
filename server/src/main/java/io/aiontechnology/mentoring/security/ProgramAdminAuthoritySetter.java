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
import io.aiontechnology.mentoring.util.SchoolUUIDMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProgramAdminAuthoritySetter implements BiFunction<Optional<SchoolPersonRole>, URI,
        List<GrantedAuthority>> {

    private final Pattern SCHOOL_PATTERN1 =
            Pattern.compile("/api/v1/schools/(.*)/");
    private final Pattern SCHOOL_PATTERN2 =
            Pattern.compile("/api/v1/schools/(.*)$");

    private final SchoolUUIDMatcher matcher;

    private Set<GrantedAuthority> basicAuthorities;

    @Override
    public List<GrantedAuthority> apply(Optional<SchoolPersonRole> role, URI requestUri) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(getBasicAuthorities());
        authorities.addAll(schoolAuthorities(role, requestUri));
        log.debug("==> Authorities: {}", authorities);
        return authorities;
    }

    private Set<GrantedAuthority> getBasicAuthorities() {
        if (basicAuthorities == null) {
            basicAuthorities = new HashSet<>();
            basicAuthorities.addAll(AuthoritiesBuilder.instance("book").withRead().withReadAll().build());
            basicAuthorities.addAll(AuthoritiesBuilder.instance("game").withRead().withReadAll().build());
            basicAuthorities.addAll(AuthoritiesBuilder.instance("person").withRead().build());
            basicAuthorities.addAll(AuthoritiesBuilder.instance("resource").withRead().build());
        }
        return basicAuthorities;
    }

    private Set<GrantedAuthority> schoolAuthorities(Optional<SchoolPersonRole> role, URI requestUri) {
        return role
                .map(r -> {
                    log.debug("==> User has authorities for school {}", r.getSchool().getId());
                    Set<GrantedAuthority> authorities = new HashSet<>();
                    if (matcher.match(requestUri, r.getSchool().getId())) {
                        log.debug("==> Schools match");
                        authorities.addAll(AuthoritiesBuilder.instance("mentor").withAll().build());
                        authorities.addAll(AuthoritiesBuilder.instance("personnel").withAll().build());
                        authorities.addAll(AuthoritiesBuilder.instance("school").withRead().withUpdate().build());
                        authorities.addAll(AuthoritiesBuilder.instance("schoolresources").withAll().build());
                        authorities.addAll(AuthoritiesBuilder.instance("schoolsession").withAll().build());
                        authorities.addAll(AuthoritiesBuilder.instance("schoolsessionstudents").withAll().build());
                        authorities.addAll(AuthoritiesBuilder.instance("student").withAll().build());
                        authorities.addAll(AuthoritiesBuilder.instance("teacher").withAll().build());
                    }
                    return authorities;
                })
                .orElseThrow(() -> new DisabledException("User in token does not match user in database"));
    }

}
