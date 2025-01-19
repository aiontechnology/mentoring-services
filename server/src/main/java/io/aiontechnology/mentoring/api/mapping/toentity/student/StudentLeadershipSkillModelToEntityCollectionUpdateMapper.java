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
import io.aiontechnology.mentoring.entity.StudentLeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentLeadershipSkill;
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
public class StudentLeadershipSkillModelToEntityCollectionUpdateMapper implements
        OneWayToCollectionUpdateMapper<InboundStudentLeadershipSkill, StudentLeadershipSkill> {

    private final OneWayMapper<String, LeadershipSkill> leadershipSkillModelToEntityMapper;

    private final OneWayMapper<URI, SchoolPersonRole> teacherModelToEntityMapper;

    private final CollectionSynchronizer<StudentLeadershipSkill> syncHelper;

    @Override
    public Collection<StudentLeadershipSkill> map(InboundStudentLeadershipSkill inboundStudentLeadershipSkill,
            Collection<StudentLeadershipSkill> studentLeadershipSkills) {
        Collection<StudentLeadershipSkill> newCollection = map(inboundStudentLeadershipSkill);
        return syncHelper.sync(studentLeadershipSkills, newCollection);
    }

    private Collection<StudentLeadershipSkill> map(InboundStudentLeadershipSkill inboundStudentLeadershipSkill) {
        return inboundStudentLeadershipSkill.getLeadershipSkills().stream()
                .map(leadershipSkillModel -> {
                    StudentLeadershipSkill studentLeadershipSkill = new StudentLeadershipSkill();
                    studentLeadershipSkill.setRole(teacherModelToEntityMapper
                            .map(inboundStudentLeadershipSkill.getTeacher())
                            .orElseThrow(() -> new NotFoundException("Unable to find teacher")));
                    studentLeadershipSkill.setLeadershipSkill(leadershipSkillModelToEntityMapper.
                            map(leadershipSkillModel)
                            .orElseThrow(() -> new NotFoundException("Invalid leadership skill: " + leadershipSkillModel)));
                    return studentLeadershipSkill;
                })
                .collect(Collectors.toList());
    }

}
