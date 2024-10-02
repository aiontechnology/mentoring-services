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

package io.aiontechnology.mentoring.configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 0.8.0
 */
public class CognitoClaimAdaptorTest {

//    @Test
//    void testConvert_SystemAdmin() throws Exception {
//        // setup the fixture
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("cognito:groups", Arrays.asList("SYSTEM_ADMIN"));
//
//        CognitoClaimConverterService cognitoClaimConverterService = new CognitoClaimConverterService(
//                new UserClaimConverter(), new GroupClaimConverter()
//        );
//
//        // execute the SUT
//        Map<String, Object> result = cognitoClaimConverterService.convert(claims);
//
//        // validation
//        List<String> authorities = (List) result.get("authorities");
//        assertThat(authorities).isNotNull();
//        assertThat(authorities.size()).isEqualTo(44);
//        assertThat(authorities.contains("book:read"));
//        assertThat(authorities.contains("book:update"));
//        assertThat(authorities.contains("game:read"));
//        assertThat(authorities.contains("mentor:read"));
//        assertThat(authorities.contains("mentor:update"));
//        assertThat(authorities.contains("person:read"));
//        assertThat(authorities.contains("personnel:read"));
//        assertThat(authorities.contains("personnel:update"));
//        assertThat(authorities.contains("program-admin:read"));
//        assertThat(authorities.contains("program-admin:update"));
//        assertThat(authorities.contains("resources:read"));
//        assertThat(authorities.contains("resources:update"));
//        assertThat(authorities.contains("school:read"));
//        assertThat(authorities.contains("school:update"));
//        assertThat(authorities.contains("student:read"));
//        assertThat(authorities.contains("student:update"));
//        assertThat(authorities.contains("teacher:read"));
//        assertThat(authorities.contains("teacher:update"));
//    }
//
//    @Test
//    void testConvert_ProgramAdmin() throws Exception {
//        // setup the fixture
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("cognito:groups", Arrays.asList("PROGRAM_ADMIN"));
//
//        CognitoClaimConverterService cognitoClaimConverterService = new CognitoClaimConverterService();
//
//        // execute the SUT
//        Map<String, Object> result = cognitoClaimConverterService.convert(claims);
//
//        // validation
//        List<String> authorities = (List) result.get("authorities");
//        assertThat(authorities).isNotNull();
//        assertThat(authorities.size()).isEqualTo(18);
//        assertThat(authorities.contains("book:read"));
//        assertThat(authorities.contains("book:update"));
//        assertThat(authorities.contains("book:create"));
//        assertThat(authorities.contains("books:read"));
//        assertThat(authorities.contains("book:delete"));
//        assertThat(authorities.contains("game:read"));
//        assertThat(authorities.contains("game:update"));
//        assertThat(authorities.contains("game:create"));
//        assertThat(authorities.contains("games:read"));
//        assertThat(authorities.contains("game:delete"));
//        assertThat(authorities.contains("mentor:read"));
//        assertThat(authorities.contains("mentor:update"));
//        assertThat(authorities.contains("mentor:create"));
//        assertThat(authorities.contains("mentors:read"));
//        assertThat(authorities.contains("mentor:delete"));
//        assertThat(authorities.contains("person:read"));
//        assertThat(authorities.contains("person:create"));
//        assertThat(authorities.contains("personnel:read"));
//        assertThat(authorities.contains("personnel:update"));
//        assertThat(authorities.contains("personnel:create"));
//        assertThat(authorities.contains("personnels:read"));
//        assertThat(authorities.contains("personnel:delete"));
//        assertThat(authorities.contains("program-admin:read"));
//        assertThat(authorities.contains("program-admin:update"));
//        assertThat(authorities.contains("program-admin:create"));
//        assertThat(authorities.contains("program-admins:read"));
//        assertThat(authorities.contains("program-admin:delete"));
//        assertThat(authorities.contains("resources:read"));
//        assertThat(authorities.contains("school:read"));
//        assertThat(authorities.contains("school:update"));
//        assertThat(authorities.contains("school:create"));
//        assertThat(authorities.contains("schools:read"));
//        assertThat(authorities.contains("school:delete"));
//        assertThat(authorities.contains("student:read"));
//        assertThat(authorities.contains("student:update"));
//        assertThat(authorities.contains("student:create"));
//        assertThat(authorities.contains("students:read"));
//        assertThat(authorities.contains("student:delete"));
//        assertThat(authorities.contains("teacher:read"));
//        assertThat(authorities.contains("teacher:update"));
//        assertThat(authorities.contains("teacher:create"));
//        assertThat(authorities.contains("teachers:read"));
//        assertThat(authorities.contains("teacher:delete"));
//    }

}
