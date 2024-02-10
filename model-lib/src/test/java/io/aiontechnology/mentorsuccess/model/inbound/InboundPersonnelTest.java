/*
 * Copyright 2022-2024 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.model.inbound;

import jakarta.validation.ConstraintViolation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static io.aiontechnology.mentorsuccess.model.enumeration.RoleType.SOCIAL_WORKER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class InboundPersonnelTest extends BaseValidatorTest {

    private static Stream<ImmutablePair<InboundPersonnel, String>> personnelInstanceProvider() {
        InboundPersonnel nullType = InboundPersonnel.builder()
                .withType(null)
                .withFirstName("FIRST")
                .withLastName("LAST")
                .build();
        InboundPersonnel nullFirstName = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName(null)
                .withLastName("LAST")
                .build();
        InboundPersonnel firstNameTooLong = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName("123456789012345678901234567890123456789012345678901") // 51 characters
                .withLastName("LAST")
                .build();
        InboundPersonnel nullLastName = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName("FIRST")
                .withLastName(null)
                .build();
        InboundPersonnel lastNameTooLong = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName("FIRST")
                .withLastName("123456789012345678901234567890123456789012345678901") // 51 characters
                .build();
        InboundPersonnel invalidEmail = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withEmail("invalid")
                .build();
        InboundPersonnel invalidWorkPhone = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withWorkPhone("invalid")
                .build();
        InboundPersonnel invalidCellPhone = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName("FIRST")
                .withLastName("LAST")
                .withCellPhone("invalid")
                .build();
        return Stream.of(ImmutablePair.of(nullType, "{personnel.type.notNull}"),
                ImmutablePair.of(nullFirstName, "{personnel.firstName.notNull}"),
                ImmutablePair.of(firstNameTooLong, "{personnel.firstName.size}"),
                ImmutablePair.of(nullLastName, "{personnel.lastName.notNull}"),
                ImmutablePair.of(lastNameTooLong, "{personnel.lastName.size}"),
                ImmutablePair.of(invalidEmail, "{personnel.email.invalid}"),
                ImmutablePair.of(invalidWorkPhone, "{personnel.workPhone.invalid}"),
                ImmutablePair.of(invalidCellPhone, "{personnel.cellPhone.invalid}"));
    }

    @ParameterizedTest
    @MethodSource("personnelInstanceProvider")
    void testInvalid(Pair<InboundPersonnel, String> personnelInstance) {
        // set up the fixture

        // execute the SUT
        Set<ConstraintViolation<InboundPersonnel>> constraintViolations =
                getValidator().validate(personnelInstance.getLeft());

        // validation
        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.iterator().next().getMessage()).isEqualTo(personnelInstance.getRight());
    }

    @Test
    void testValid() {
        // set up the fixture
        InboundPersonnel inboundPersonnel = InboundPersonnel.builder()
                .withType(SOCIAL_WORKER)
                .withFirstName("FIRST")
                .withLastName("LAST")
                .build();

        // execute the SUT
        Set<ConstraintViolation<InboundPersonnel>> constraintViolations = getValidator().validate(inboundPersonnel);

        // validation
        assertThat(constraintViolations.size()).isEqualTo(0);
    }

}
