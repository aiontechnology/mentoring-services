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

package io.aiontechnology.mentorsuccess.feature.workflow.process;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@Component
@RequiredArgsConstructor
public class TaskCompletionConsumer implements BiConsumer<UUID, Map<String, Object>> {

    private final RuntimeService runtimeService;
    private final TaskService taskService;

    @Override
    public void accept(UUID processId, Map<String, Object> variables) {
        processTaskCompletion(processId, variables);
    }

    private void completeTask(Task task, UUID processId, Map<String, Object> variables) {
        runtimeService.setVariables(processId.toString(), variables);
        taskService.complete(task.getId());
    }

    private TaskQuery createTaskQuery(UUID processId) {
        return taskService.createTaskQuery()
                .processInstanceId(processId.toString())
                .includeProcessVariables();
    }

    private void processTaskCompletion(UUID processId, Map<String, Object> variables) {
        var query = createTaskQuery(processId);
        query.list().stream()
                .findFirst()
                .ifPresent(task -> completeTask(task, processId, variables));
    }

}
