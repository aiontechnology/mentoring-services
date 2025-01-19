/*
 * Copyright 2022-2023 Aion Technology LLC
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

package io.aiontechnology.mentoring.workflow;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.model.enumeration.ContactMethod;
import io.aiontechnology.mentoring.model.enumeration.ResourceLocation;
import io.aiontechnology.mentoring.model.inbound.student.InboundContact;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentRegistration;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentTeacher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationToInboundStudentMapper implements OneWayMapper<InboundStudentRegistration, InboundStudent> {

    @Override
    public Optional<InboundStudent> map(InboundStudentRegistration studentRegistration) {
        return Optional.ofNullable(studentRegistration)
                .map(registration -> {
                    List<InboundContact> contacts = new ArrayList<>();
                    mapParent1(studentRegistration).ifPresent(contacts::add);
                    mapParent2(studentRegistration).ifPresent(contacts::add);
                    mapEmergency(studentRegistration).ifPresent(contacts::add);
                    return InboundStudent.builder()
                            .withFirstName(studentRegistration.getStudentFirstName())
                            .withLastName(studentRegistration.getStudentLastName())
                            .withGrade(studentRegistration.getGrade())
                            .withPreferredTime(studentRegistration.getPreferredSession())
                            .withRegistrationSigned(!studentRegistration.getParentSignature().equals(null) &&
                                    !studentRegistration.getParentSignature().equals(""))
                            .withContacts(contacts)
                            .withTeacher(mapTeacher(studentRegistration))
                            .withLocation(ResourceLocation.OFFLINE)
                            .withMediaReleaseSigned(false)
                            .build();

                });
    }

    private ContactMethod mapContactMethod(String method) {
        try {
            return ContactMethod.valueOf(method);
        } catch (Exception e) {
            return null;
        }
    }

    private Optional<InboundContact> mapEmergency(InboundStudentRegistration studentRegistration) {
        if (studentRegistration.getEmergencyContactFirstName() == null || studentRegistration.getEmergencyContactFirstName() == "") {
            return Optional.empty();
        }
        return Optional.ofNullable(studentRegistration)
                .map(registration -> InboundContact.builder()
                        .withFirstName(studentRegistration.getEmergencyContactFirstName())
                        .withLastName(studentRegistration.getEmergencyContactLastName())
                        .withPhone(studentRegistration.getEmergencyContactPhone())
                        .withIsEmergencyContact(true)
                        .build());
    }

    private Optional<InboundContact> mapParent1(InboundStudentRegistration studentRegistration) {
        if (studentRegistration.getParent1FirstName() == null || studentRegistration.getParent1FirstName().equals("")) {
            return Optional.empty();
        }
        return Optional.ofNullable(studentRegistration)
                .map(registration -> InboundContact.builder()
                        .withFirstName(registration.getParent1FirstName())
                        .withLastName(registration.getParent1LastName())
                        .withPhone(registration.getParent1PhoneNumber())
                        .withEmail(registration.getParent1EmailAddress())
                        .withPreferredContactMethod(mapContactMethod(registration.getParent1PreferredContactMethod()))
                        .build());
    }

    private Optional<InboundContact> mapParent2(InboundStudentRegistration studentRegistration) {
        if (studentRegistration.getParent2FirstName() == null || studentRegistration.getParent2FirstName().equals("")) {
            return Optional.empty();
        }
        return Optional.ofNullable(studentRegistration)
                .map(registration -> InboundContact.builder()
                        .withFirstName(registration.getParent2FirstName())
                        .withLastName(registration.getParent2LastName())
                        .withPhone(registration.getParent2PhoneNumber())
                        .withEmail(registration.getParent2EmailAddress())
                        .withPreferredContactMethod(mapContactMethod(registration.getParent2PreferredContactMethod()))
                        .build());
    }

    private InboundStudentTeacher mapTeacher(InboundStudentRegistration studentRegistration) {
        if (studentRegistration == null || studentRegistration.getTeacher() == null) {
            return null;
        }
        return InboundStudentTeacher.builder()
                .withUri(studentRegistration.getTeacher())
                .build();
    }

}
