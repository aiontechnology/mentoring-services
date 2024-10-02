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

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@RequiredArgsConstructor(access = PRIVATE)
public class AuthoritiesBuilder {

    private final Set<GrantedAuthority> authorities = new HashSet<>();
    private final String resource;

    public static AuthoritiesBuilder instance(String resource) {
        return new AuthoritiesBuilder(resource);
    }

    public AuthoritiesBuilder withAll() {
        this.withCreate()
                .withRead()
                .withReadAll()
                .withUpdate()
                .withDelete();
        return this;
    }

    public AuthoritiesBuilder withCreate() {
        authorities.add(new SimpleGrantedAuthority(resource + ":create"));
        return this;
    }

    public AuthoritiesBuilder withRead() {
        authorities.add(new SimpleGrantedAuthority(resource + ":read"));
        return this;
    }

    public AuthoritiesBuilder withReadAll() {
        authorities.add(new SimpleGrantedAuthority(resource + "s:read"));
        return this;
    }

    public AuthoritiesBuilder withUpdate() {
        authorities.add(new SimpleGrantedAuthority(resource + ":update"));
        return this;
    }

    public AuthoritiesBuilder withDelete() {
        authorities.add(new SimpleGrantedAuthority(resource + ":delete"));
        return this;
    }

    public Set<GrantedAuthority> build() {
        return authorities;
    }

}
