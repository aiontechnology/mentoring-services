/*
 * Copyright 2021-2024 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentoring.security;

import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@Slf4j
public class AuthoritiesGrantingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("==> Managing authorities");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        URI uri = URI.create(httpServletRequest.getRequestURI());

        getJwtAuthenticationToken().ifPresent(auth -> {
            Optional<SchoolPersonRole> roleSupplier = auth.getToken().getClaim("username");
            BiFunction<Optional<SchoolPersonRole>, URI, List<GrantedAuthority>> authSetter =
                    auth.getToken().getClaim("cognito:groups");
            List<GrantedAuthority> authorities = authSetter != null
                    ? authSetter.apply(roleSupplier, uri)
                    : Collections.emptyList();
            log.debug("==> Set authorities: {}", authorities);

            JwtAuthenticationToken newAuth = new JwtAuthenticationToken(auth.getToken(), authorities, auth.getName());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        });

        chain.doFilter(request, response);
    }

    private Optional<JwtAuthenticationToken> getJwtAuthenticationToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null && authentication.getClass().isAssignableFrom(JwtAuthenticationToken.class)) {
                return Optional.of((JwtAuthenticationToken) authentication);
            }
        }
        return Optional.empty();
    }

}
