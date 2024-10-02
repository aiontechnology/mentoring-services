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

package io.aiontechnology.mentoring.api.controller;

import io.aiontechnology.mentoring.model.inbound.InboundInvitation;
import io.aiontechnology.mentoring.service.SchoolService;
import io.aiontechnology.mentoring.service.StudentInvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller that vends a REST interface for inviting students.
 *
 * @author Whitney Hunter
 * @since 1.10.0
 */
@RestController
@RequestMapping("/api/v1/schools/{schoolId}/invitations")
@RequiredArgsConstructor
@Slf4j
public class StudentInvitationController {

    // Services
    private final StudentInvitationService studentInvitationService;
    private final SchoolService schoolService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('school:update')")
    public InboundInvitation studentInvitation(@PathVariable("schoolId") UUID schoolId,
            @RequestBody @Valid InboundInvitation invitation) {
        schoolService.getSchoolById(schoolId)
                .ifPresent(school -> studentInvitationService.invite(invitation, school));
        return invitation;
    }

}
