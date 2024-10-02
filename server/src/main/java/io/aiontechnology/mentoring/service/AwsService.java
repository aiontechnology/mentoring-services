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

package io.aiontechnology.mentoring.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupResult;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.model.inbound.InboundProgramAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.amazonaws.services.cognitoidp.model.DeliveryMediumType.EMAIL;
import static java.lang.Boolean.FALSE;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AwsService {

    private static final String PROGRAM_ADMIN_GROUP = "PROGRAM_ADMIN";
    private static final String EMAIL_KEY = "email";
    private static final String FIRST_NAME_KEY = "given_name";
    private static final String LAST_NAME_KEY = "family_name";
    private static final String VERIFIED_EMAIL_KEY = "email_verified";
    private static final String SCHOOL_UUID_KEY = "custom:school_uuid";

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    private final AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    public Pair<InboundProgramAdmin, UUID> createAwsUser(UUID schoolId, InboundProgramAdmin programAdmin) {

        AdminCreateUserResult addUserResult = doCreateAwsUser(schoolId, programAdmin);
        log.debug("Sent add user request. Result: {}", addUserResult);

        AdminAddUserToGroupResult addUserToGroupResult = doAddAwsUserToGroup(programAdmin);
        log.debug("Sent add user to group request. Result: {}", addUserToGroupResult);

        return Pair.of(programAdmin, UUID.fromString(addUserResult.getUser().getUsername()));
    }

    public InboundProgramAdmin updateAwsUser(InboundProgramAdmin programAdmin) {
        AdminUpdateUserAttributesResult updateUserAttributesResult = doUpdateAwsUser(programAdmin);
        log.debug("Sent update user attributes request. Result: {}", updateUserAttributesResult);
        return programAdmin;
    }

    public SchoolPersonRole removeAwsUser(SchoolPersonRole role) {
        AdminDeleteUserResult deleteUserResult = deleteAwsUser(role);
        log.debug("Sent delete user request. Result: {}", deleteUserResult);
        return role;
    }

    private AdminCreateUserResult doCreateAwsUser(UUID schoolId, InboundProgramAdmin programAdmin) {
        log.debug("Adding a program admin to AWS");
        AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(programAdmin.getEmail())
                .withUserAttributes(
                        new AttributeType()
                                .withName(EMAIL_KEY)
                                .withValue(programAdmin.getEmail()),
                        new AttributeType()
                                .withName(FIRST_NAME_KEY)
                                .withValue(programAdmin.getFirstName()),
                        new AttributeType()
                                .withName(LAST_NAME_KEY)
                                .withValue(programAdmin.getLastName()),
                        new AttributeType()
                                .withName(VERIFIED_EMAIL_KEY)
                                .withValue("true"),
                        new AttributeType()
                                .withName(SCHOOL_UUID_KEY)
                                .withValue(schoolId.toString()))
                .withDesiredDeliveryMediums(EMAIL)
                .withForceAliasCreation(FALSE);
        return awsCognitoIdentityProvider.adminCreateUser(cognitoRequest);
    }

    private AdminAddUserToGroupResult doAddAwsUserToGroup(InboundProgramAdmin programAdmin) {
        AdminAddUserToGroupRequest cognitoRequest = new AdminAddUserToGroupRequest()
                .withUsername(programAdmin.getEmail())
                .withUserPoolId(userPoolId)
                .withGroupName(PROGRAM_ADMIN_GROUP);
        return awsCognitoIdentityProvider.adminAddUserToGroup(cognitoRequest);
    }

    private AdminUpdateUserAttributesResult doUpdateAwsUser(InboundProgramAdmin programAdmin) {
        AdminUpdateUserAttributesRequest cognitoRequest = new AdminUpdateUserAttributesRequest()
                .withUsername(programAdmin.getEmail())
                .withUserPoolId(userPoolId)
                .withUserAttributes(
                        new AttributeType()
                                .withName(FIRST_NAME_KEY)
                                .withValue(programAdmin.getFirstName()),
                        new AttributeType()
                                .withName(LAST_NAME_KEY)
                                .withValue(programAdmin.getLastName())

                );
        return awsCognitoIdentityProvider.adminUpdateUserAttributes(cognitoRequest);
    }

    private AdminDeleteUserResult deleteAwsUser(SchoolPersonRole role) {
        AdminDeleteUserRequest cognitoRequest = new AdminDeleteUserRequest()
                .withUsername(role.getPerson().getEmail())
                .withUserPoolId(userPoolId);
        return awsCognitoIdentityProvider.adminDeleteUser(cognitoRequest);
    }

}
