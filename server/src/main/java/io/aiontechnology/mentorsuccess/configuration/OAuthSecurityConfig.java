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

package io.aiontechnology.mentorsuccess.configuration;

import io.aiontechnology.mentorsuccess.security.AuthoritiesGrantingFilter;
import io.aiontechnology.mentorsuccess.security.CognitoClaimConverterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;

/**
 * @author Whitney Hunter
 * @since 0.8.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true,
        jsr250Enabled = true)
@Slf4j
@RequiredArgsConstructor
public class OAuthSecurityConfig {

    private final CognitoClaimConverterService cognitoClaimConverterService;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String jwkSetUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        jwtDecoder.setClaimSetConverter(cognitoClaimConverterService);
        return jwtDecoder;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/api/v1/schools/*/registrations/**")
                .requestMatchers("/api/v1/schools/*/students/*/assessments/*")
                .requestMatchers("/api/v1/schools/*/students/*/registrations/*")
                .requestMatchers("/api/v1/schools/*/students/*/workflow/assessments/*")
                .requestMatchers(GET, "/api/v1/schools/*/programAdmins")
                .requestMatchers("/api/v1/behaviors")
                .requestMatchers("/api/v1/leadership_skills")
                .requestMatchers("/api/v1/leadership_traits");
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2ResourceServer(oAuth2ResourceServerCustomizer ->
                        oAuth2ResourceServerCustomizer.jwt(jwtCustomizer -> jwtCustomizer.decoder(jwtDecoder())))
                .addFilterAfter(new AuthoritiesGrantingFilter(), BasicAuthenticationFilter.class);
        return http.build();
    }

}
