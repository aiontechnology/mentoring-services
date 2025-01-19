/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayToCollectionUpdateMapper;
import io.aiontechnology.atlas.synchronization.CollectionSynchronizer;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.StudentLeadershipTrait;
import io.aiontechnology.mentoring.entity.reference.LeadershipTrait;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipTrait;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class StudentLeadershipTraitModelToEntityCollectionUpdateMapper implements
        OneWayToCollectionUpdateMapper<InboundStudentLeadershipTrait, StudentLeadershipTrait> {

    private final OneWayMapper<String, LeadershipTrait> leadershipTraitModelToEntityMapper;

    private final OneWayMapper<URI, SchoolPersonRole> personModelToEntityMapper;

    private final CollectionSynchronizer<StudentLeadershipTrait> syncHelper;

    @Override
    public Collection<StudentLeadershipTrait> map(InboundStudentLeadershipTrait inboundStudentLeadershipTrait,
            Collection<StudentLeadershipTrait> studentLeadershipTraits) {
        Collection<StudentLeadershipTrait> newCollection = map(inboundStudentLeadershipTrait);
        return syncHelper.sync(studentLeadershipTraits, newCollection);
    }

    private Collection<StudentLeadershipTrait> map(InboundStudentLeadershipTrait inboundStudentLeadershipTrait) {
        return inboundStudentLeadershipTrait.getLeadershipTraits().stream()
                .map(leadershipTraitModel -> {
                    StudentLeadershipTrait studentLeadershipTrait = new StudentLeadershipTrait();
                    studentLeadershipTrait.setRole(personModelToEntityMapper
                            .map(inboundStudentLeadershipTrait.getTeacher())
                            .orElseThrow(() -> new NotFoundException("Unable to find teacher")));
                    studentLeadershipTrait.setLeadershipTrait(leadershipTraitModelToEntityMapper.
                            map(leadershipTraitModel)
                            .orElseThrow(() -> new NotFoundException("Invalid leadership trait: " + leadershipTraitModel)));
                    return studentLeadershipTrait;
                })
                .collect(Collectors.toList());
    }

}
