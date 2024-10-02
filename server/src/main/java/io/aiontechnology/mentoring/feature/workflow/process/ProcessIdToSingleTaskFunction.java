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

package io.aiontechnology.mentoring.feature.workflow.process;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProcessIdToSingleTaskFunction implements Function<UUID, Optional<Task>> {

    // Services
    private final TaskService taskService;

    public Optional<Task> apply(UUID processId) {
        TaskQuery query = taskService.createTaskQuery()
                .processInstanceId(processId.toString())
                .includeProcessVariables();
        var tasks = query.list();
        if (tasks.size() > 1) {
            throw new IllegalStateException("Found more than one task for process ID: " + processId);
        }
        return tasks.stream().findFirst();
    }

}
