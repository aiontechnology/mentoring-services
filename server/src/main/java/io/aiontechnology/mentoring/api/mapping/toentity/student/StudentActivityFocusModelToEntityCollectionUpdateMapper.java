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

package io.aiontechnology.mentoring.api.mapping.toentity.student;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayToCollectionUpdateMapper;
import io.aiontechnology.atlas.synchronization.CollectionSynchronizer;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.SchoolPersonRole;
import io.aiontechnology.mentoring.entity.StudentActivityFocus;
import io.aiontechnology.mentoring.entity.reference.ActivityFocus;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentActivityFocus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentActivityFocusModelToEntityCollectionUpdateMapper implements
        OneWayToCollectionUpdateMapper<InboundStudentActivityFocus, StudentActivityFocus> {

    private final OneWayMapper<String, ActivityFocus> activityFocusModelToEntityMapper;

    private final OneWayMapper<URI, SchoolPersonRole> teacherModelToEntityMapper;

    private final CollectionSynchronizer<StudentActivityFocus> syncHelper;

    @Override
    public Collection<StudentActivityFocus> map(InboundStudentActivityFocus inboundStudentActivityFocus,
            Collection<StudentActivityFocus> studentActivityFocuses) {
        Collection<StudentActivityFocus> newCollection = map(inboundStudentActivityFocus);
        return syncHelper.sync(studentActivityFocuses, newCollection);
    }

    private Collection<StudentActivityFocus> map(InboundStudentActivityFocus inboundStudentActivityFocus) {
        return inboundStudentActivityFocus.getActivityFocuses().stream()
                .map(activityFocusModel -> {
                    StudentActivityFocus studentActivityFocus = new StudentActivityFocus();
                    studentActivityFocus.setRole(teacherModelToEntityMapper
                            .map(inboundStudentActivityFocus.getTeacher())
                            .orElseThrow(() -> new NotFoundException("Unable to find teacher")));
                    studentActivityFocus.setActivityFocus(activityFocusModelToEntityMapper.
                            map(activityFocusModel)
                            .orElseThrow(() -> new NotFoundException("Invalid activity focus: " + activityFocusModel)));
                    return studentActivityFocus;
                })
                .collect(Collectors.toList());
    }

}
