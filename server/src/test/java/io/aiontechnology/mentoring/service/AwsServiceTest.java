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

package io.aiontechnology.mentoring.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.model.inbound.InboundProgramAdmin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Whitney Hunter
 * @since 0.12.0
 */
public class AwsServiceTest {

    @Test
    @Disabled("Can't call Cognito in tests")
    void testCreateAwsUser() throws Exception {
        // set up the fixture
        AWSCognitoIdentityProvider awsCognitoIdentityProvider = mock(AWSCognitoIdentityProvider.class);
        AwsService awsService = new AwsService(awsCognitoIdentityProvider);

        String userPoolId = "USER_POOL";
        Field userPoolIdField = AwsService.class.getDeclaredField("userPoolId");
        userPoolIdField.setAccessible(true);
        ReflectionUtils.setField(userPoolIdField, awsService, userPoolId);

        UUID schoolId = UUID.randomUUID();
        String email = "test@example.com";
        String firstName = "FIRST";
        String lastName = "LAST";
        InboundProgramAdmin inboundProgramAdmin = InboundProgramAdmin.builder()
                .withEmail(email)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        // execute the SUT
        awsService.createAwsUser(schoolId, inboundProgramAdmin);

        // validation
        ArgumentCaptor<AdminCreateUserRequest> userRequestArgumentCaptor =
                ArgumentCaptor.forClass(AdminCreateUserRequest.class);
        verify(awsCognitoIdentityProvider).adminCreateUser(userRequestArgumentCaptor.capture());
        AdminCreateUserRequest adminCreateUserRequest = userRequestArgumentCaptor.getValue();
        assertThat(adminCreateUserRequest.getUserPoolId()).isEqualTo(userPoolId);
        assertThat(adminCreateUserRequest.getUsername()).isEqualTo(email);
        assertThat(adminCreateUserRequest.getDesiredDeliveryMediums()).isEqualTo(Arrays.asList("EMAIL"));
        assertThat(adminCreateUserRequest.getForceAliasCreation()).isEqualTo(FALSE);
        List<AttributeType> attributeTypeList = adminCreateUserRequest.getUserAttributes();
        assertThat(attributeTypeList.size()).isEqualTo(5);
        assertThat(attributeTypeList).contains(new AttributeType().withName("email").withValue(email));
        assertThat(attributeTypeList).contains(new AttributeType().withName("given_name").withValue(firstName));
        assertThat(attributeTypeList).contains(new AttributeType().withName("family_name").withValue(lastName));
        assertThat(attributeTypeList).contains(new AttributeType().withName("email_verified").withValue("true"));
        assertThat(attributeTypeList).contains(new AttributeType().withName("custom:school_uuid").withValue(schoolId.toString()));

        ArgumentCaptor<AdminAddUserToGroupRequest> groupRequestArgumentCaptor =
                ArgumentCaptor.forClass(AdminAddUserToGroupRequest.class);
        verify(awsCognitoIdentityProvider).adminAddUserToGroup(groupRequestArgumentCaptor.capture());
        AdminAddUserToGroupRequest adminAddUserToGroupRequest = groupRequestArgumentCaptor.getValue();
        assertThat(adminAddUserToGroupRequest.getUserPoolId()).isEqualTo(userPoolId);
        assertThat(adminAddUserToGroupRequest.getUsername()).isEqualTo(email);
        assertThat(adminAddUserToGroupRequest.getGroupName()).isEqualTo("PROGRAM_ADMIN");
    }

    @Test
    void testUpdateAwsUser() throws Exception {
        // set up the fixture
        AWSCognitoIdentityProvider awsCognitoIdentityProvider = mock(AWSCognitoIdentityProvider.class);
        AwsService awsService = new AwsService(awsCognitoIdentityProvider);

        String userPoolId = "USER_POOL";
        Field userPoolIdField = AwsService.class.getDeclaredField("userPoolId");
        userPoolIdField.setAccessible(true);
        ReflectionUtils.setField(userPoolIdField, awsService, userPoolId);

        String email = "test@example.com";
        String firstName = "FIRST";
        String lastName = "LAST";
        InboundProgramAdmin inboundProgramAdmin = InboundProgramAdmin.builder()
                .withEmail(email)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();

        // execute the SUT
        awsService.updateAwsUser(inboundProgramAdmin);

        // validation
        ArgumentCaptor<AdminUpdateUserAttributesRequest> updateUserAttributesRequestCaptor =
                ArgumentCaptor.forClass(AdminUpdateUserAttributesRequest.class);
        verify(awsCognitoIdentityProvider).adminUpdateUserAttributes(updateUserAttributesRequestCaptor.capture());
        AdminUpdateUserAttributesRequest adminUpdateUserAttributesRequest =
                updateUserAttributesRequestCaptor.getValue();
        assertThat(adminUpdateUserAttributesRequest.getUserPoolId()).isEqualTo(userPoolId);
        assertThat(adminUpdateUserAttributesRequest.getUsername()).isEqualTo(email);
        List<AttributeType> attributeTypeList = adminUpdateUserAttributesRequest.getUserAttributes();
        assertThat(attributeTypeList.size()).isEqualTo(2);
        assertThat(attributeTypeList).contains(new AttributeType().withName("given_name").withValue(firstName));
        assertThat(attributeTypeList).contains(new AttributeType().withName("family_name").withValue(lastName));
    }

    @Test
    void testRemoveAwsUser() throws Exception {
        // set up the fixture
        AWSCognitoIdentityProvider awsCognitoIdentityProvider = mock(AWSCognitoIdentityProvider.class);
        AwsService awsService = new AwsService(awsCognitoIdentityProvider);

        String userPoolId = "USER_POOL";
        Field userPoolIdField = AwsService.class.getDeclaredField("userPoolId");
        userPoolIdField.setAccessible(true);
        ReflectionUtils.setField(userPoolIdField, awsService, userPoolId);

        String email = "test@example.com";
        Person person = new Person();
        person.setEmail(email);
        SchoolPersonRole role = new SchoolPersonRole();
        role.setPerson(person);

        // execute the SUT
        awsService.removeAwsUser(role);

        // validation
        ArgumentCaptor<AdminDeleteUserRequest> deleteUserRequestCaptor =
                ArgumentCaptor.forClass(AdminDeleteUserRequest.class);
        verify(awsCognitoIdentityProvider).adminDeleteUser(deleteUserRequestCaptor.capture());
        AdminDeleteUserRequest adminDeleteUserRequest = deleteUserRequestCaptor.getValue();
        assertThat(adminDeleteUserRequest.getUserPoolId()).isEqualTo(userPoolId);
        assertThat(adminDeleteUserRequest.getUsername()).isEqualTo(email);
    }

}
