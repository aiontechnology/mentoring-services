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

package io.aiontechnology.mentorsuccess.workflow;

import io.aiontechnology.mentorsuccess.entity.workflow.StudentInformation;
import io.aiontechnology.mentorsuccess.feature.workflow.process.ProcessIdToSingleTaskFunction;
import lombok.RequiredArgsConstructor;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentWorkflowService {

    // Services
    private final FlowableProcessUtilities flowableProcessUtilities;
    private final ProcessIdToSingleTaskFunction flowableTaskUtilities;

    /**
     * Lookup a student information flow with the given Process ID.
     *
     * @param processId The process ID of the desired student information flow.
     * @return The {@link StudentInformation} corresponding to the desired process if it could be found.
     */
    public Optional<StudentInformation> findStudentInformationWorkflowById(UUID processId) {
        Optional<Task> task = flowableTaskUtilities.apply(processId);
        return task
                .map(TaskInfo::getProcessVariables)
                .flatMap(flowableProcessUtilities::findStudent)
                .map(student -> StudentInformation.builder()
                        .id(UUID.fromString(task.get().getProcessInstanceId()))
                        .studentName(student.getFullName())
                        .build());
    }

}
