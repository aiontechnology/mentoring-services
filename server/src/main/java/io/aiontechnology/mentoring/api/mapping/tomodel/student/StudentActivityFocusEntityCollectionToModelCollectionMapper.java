/*
 * Copyright 2023 Aion Technology LLC
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

import io.aiontechnology.atlas.classification.CollectionClassifier;
import io.aiontechnology.atlas.classification.impl.FunctionBasedCollectionClassifier;
import io.aiontechnology.atlas.mapping.OneWayCollectionMapper;
import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.StudentActivityFocus;
import io.aiontechnology.mentoring.entity.reference.ActivityFocus;
import io.aiontechnology.mentoring.model.enumeration.RoleType;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentActivityFocus;
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

@Component
@RequiredArgsConstructor
public class StudentActivityFocusEntityCollectionToModelCollectionMapper implements
        OneWayCollectionMapper<StudentActivityFocus, String> {

    private final CollectionClassifier<SchoolPersonRole, StudentActivityFocus> byRoleListClassifier =
            new FunctionBasedCollectionClassifier<>(StudentActivityFocus::getRole);

    private final CollectionClassifier<RoleType, StudentActivityFocus> byTypeListClassifier =
            new FunctionBasedCollectionClassifier<>((studentActivityFocus -> studentActivityFocus.getRole().getType()));

    private final OneWayCollectionMapper<ActivityFocus, String> activityFocusEntityToModelCollectionMapper;

    //    @Qualifier("teacherAssemblerMapperAdaptor")
    private final OneWayMapper<SchoolPersonRole, TeacherResource> teacherEntityToModelMapper;

    @Override
    public Optional<Collection<String>> map(Collection<StudentActivityFocus> studentActivityFocuses) {
        Collection<OutboundStudentActivityFocus> outboundStudentActivityFocuses =
                byRoleListClassifier.classify(studentActivityFocuses).entrySet().stream()
                        .flatMap(e -> {
                            Map<RoleType, List<StudentActivityFocus>> roleTypeMap =
                                    byTypeListClassifier.classify(e.getValue());
                            return transformRoleTypeMap(e.getKey(), roleTypeMap).stream();
                        })
                        .collect(Collectors.toCollection(ArrayList::new));
        return Optional.of(outboundStudentActivityFocuses.stream().findFirst()
                .map(OutboundStudentActivityFocus::getActivityFocuses)
                .orElse(Collections.emptyList()));
    }

    private Collection<OutboundStudentActivityFocus> transformRoleTypeMap(SchoolPersonRole teacher,
            Map<RoleType, List<StudentActivityFocus>> roleTypeMap) {
        return roleTypeMap.entrySet().stream()
                .map(e -> {
                    Set<ActivityFocus> activityFocuses = e.getValue().stream()
                            .map(StudentActivityFocus::getActivityFocus)
                            .collect(Collectors.toSet());
                    return OutboundStudentActivityFocus.builder()
                            .withActivityFocuses(activityFocusEntityToModelCollectionMapper.map(activityFocuses).orElse(Collections.emptyList()))
                            .withTeacher(teacherEntityToModelMapper.map(teacher).orElse(null))
                            .build();
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
