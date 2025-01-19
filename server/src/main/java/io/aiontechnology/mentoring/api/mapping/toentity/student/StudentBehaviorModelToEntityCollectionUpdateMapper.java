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
import io.aiontechnology.mentoring.entity.StudentBehavior;
import io.aiontechnology.mentoring.entity.reference.Behavior;
import io.aiontechnology.mentoring.model.inbound.student.InboundStudentBehavior;
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
public class StudentBehaviorModelToEntityCollectionUpdateMapper implements
        OneWayToCollectionUpdateMapper<InboundStudentBehavior, StudentBehavior> {

    private final OneWayMapper<String, Behavior> behaviorModelToEntityMapper;

    private final OneWayMapper<URI, SchoolPersonRole> teacherUriToEntityMapper;

    private final CollectionSynchronizer<StudentBehavior> syncHelper;

    @Override
    public Collection<StudentBehavior> map(InboundStudentBehavior inboundStudentBehavior,
            Collection<StudentBehavior> studentBehaviors) {
        Collection<StudentBehavior> newCollection = doMap(inboundStudentBehavior);
        return syncHelper.sync(studentBehaviors, newCollection);
    }

    /**
     * Map the set of behavior strings contained in the provided {@link InboundStudentBehavior} to a collection of
     * {@link StudentBehavior} instances.
     *
     * @param inboundStudentBehavior The {@link InboundStudentBehavior} from which to get the @link InboundBehavior}
     * instances to map.
     * @return The resulting set of {@link StudentBehavior} instances.
     */
    private Collection<StudentBehavior> doMap(InboundStudentBehavior inboundStudentBehavior) {
        return inboundStudentBehavior.getBehaviors().stream()
                .map(behaviorModel -> {
                    StudentBehavior studentBehavior = new StudentBehavior();
                    studentBehavior.setRole(teacherUriToEntityMapper
                            .map(inboundStudentBehavior.getTeacher())
                            .orElseThrow(() -> new NotFoundException("Unable to find teacher")));
                    studentBehavior.setBehavior(behaviorModelToEntityMapper
                            .map(behaviorModel)
                            .orElseThrow(() -> new NotFoundException("Invalid behavior: " + behaviorModel)));
                    return studentBehavior;
                })
                .collect(Collectors.toList());
    }

}
