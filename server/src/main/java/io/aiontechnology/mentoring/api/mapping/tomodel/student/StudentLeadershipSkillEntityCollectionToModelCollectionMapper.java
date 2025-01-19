/*
 * Copyright 2020-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.mapping.tomodel.student;

import io.aiontechnology.atlas.classification.CollectionClassifier;
import io.aiontechnology.atlas.classification.impl.FunctionBasedCollectionClassifier;
import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.StudentLeadershipSkill;
import io.aiontechnology.mentoring.entity.reference.LeadershipSkill;
import io.aiontechnology.mentoring.model.enumeration.RoleType;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentLeadershipSkill;
import io.aiontechnology.mentoring.resource.TeacherResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class StudentLeadershipSkillEntityCollectionToModelCollectionMapper implements
        OneWayCollectionMapper<StudentLeadershipSkill, String> {

    private final CollectionClassifier<SchoolPersonRole, StudentLeadershipSkill> byRoleListClassifier =
            new FunctionBasedCollectionClassifier<>(StudentLeadershipSkill::getRole);

    private final CollectionClassifier<RoleType, StudentLeadershipSkill> byTypeListClassifier =
            new FunctionBasedCollectionClassifier<>((studentLeadershipSkill -> studentLeadershipSkill.getRole().getType()));

    private final OneWayCollectionMapper<LeadershipSkill, String> leadershipSkillEntityToModelCollectionMapper;

    //    @Qualifier("teacherAssemblerMapperAdaptor")
    private final OneWayMapper<SchoolPersonRole, TeacherResource> teacherEntityToModelMapper;

    @Override
    public Optional<Collection<String>> map(Collection<StudentLeadershipSkill> studentLeadershipSkills) {
        Collection<OutboundStudentLeadershipSkill> outboundStudentLeadershipSkills =
                byRoleListClassifier.classify(studentLeadershipSkills).entrySet().stream()
                        .flatMap(e -> {
                            Map<RoleType, List<StudentLeadershipSkill>> roleTypeMap =
                                    byTypeListClassifier.classify(e.getValue());
                            return transformRoleTypeMap(e.getKey(), roleTypeMap).stream();
                        })
                        .collect(Collectors.toCollection(ArrayList::new));
        return Optional.of(outboundStudentLeadershipSkills.stream().findFirst()
                .map(OutboundStudentLeadershipSkill::getLeadershipSkills)
                .orElse(Collections.emptyList()));
    }

    private Collection<OutboundStudentLeadershipSkill> transformRoleTypeMap(SchoolPersonRole teacher,
            Map<RoleType, List<StudentLeadershipSkill>> roleTypeMap) {
        return roleTypeMap.entrySet().stream()
                .map(e -> {
                    Set<LeadershipSkill> leadershipSkills = e.getValue().stream()
                            .map(StudentLeadershipSkill::getLeadershipSkill)
                            .collect(Collectors.toSet());
                    return OutboundStudentLeadershipSkill.builder()
                            .withLeadershipSkills(leadershipSkillEntityToModelCollectionMapper.map(leadershipSkills).orElse(Collections.emptyList()))
                            .withTeacher(teacherEntityToModelMapper.map(teacher).orElse(null))
                            .build();
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
