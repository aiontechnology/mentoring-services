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

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@Slf4j
@Service
public class CognitoClaimConverterService implements Converter<Map<String, Object>, Map<String, Object>> {

    private final String USERNAME_CLAIM = "username";
    private final String GROUP_CLAIM = "cognito:groups";

    private MappedJwtClaimSetConverter jwtClaimSetConverter;

    public CognitoClaimConverterService(UserClaimConverter userClaimConverter,
            GroupClaimConverter groupClaimConverter) {
        Map<String, Converter<Object, ?>> converters = new HashMap<>();
        converters.put(USERNAME_CLAIM, userClaimConverter);
        converters.put(GROUP_CLAIM, groupClaimConverter);
        jwtClaimSetConverter = MappedJwtClaimSetConverter.withDefaults(converters);
    }

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        return jwtClaimSetConverter.convert(claims);
    }

}
