/*
 * Copyright 2020-2023 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.mapping.tomodel.student;

import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.StudentBehavior;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentSchoolSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Component
@RequiredArgsConstructor
public class StudentSchoolSessionEntityToModelMapper implements
        OneWayMapper<StudentSchoolSession, OutboundStudentSchoolSession> {

    private final OneWayCollectionMapper<StudentBehavior, String> studentBehaviorEntityToModelMapper;

    private final OneWayCollectionMapper<Interest, String> interestModelToEntityMapper;

    @Override
    public Optional<OutboundStudentSchoolSession> map(StudentSchoolSession studentSchoolSession) {
        return Optional.ofNullable(studentSchoolSession)
                .map(ss -> OutboundStudentSchoolSession.builder()
                        .withGrade(ss.getGrade())
                        .withPreferredTime(ss.getPreferredTime())
                        .withActualTime(ss.getActualTime())
                        .withLocation(ss.getLocation())
                        .withRegistrationSigned(ss.getIsRegistrationSigned())
                        .withMediaReleaseSigned(ss.getIsMediaReleaseSigned())
                        .withStartDate(ss.getStartDate())
                        .withBehaviors(studentBehaviorEntityToModelMapper.map(ss.getStudentBehaviors()).orElse(Collections.emptyList()))
                        .withInterests(interestModelToEntityMapper.map(ss.getInterests()).orElse(Collections.emptyList()))
                        .build()
                );
    }

}
