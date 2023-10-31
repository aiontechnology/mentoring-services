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

import io.aiontechnology.mentorsuccess.feature.workflow.process.ProcessIdToSingleTaskFunction;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessIdToSingleTaskFunctionTest {

    @Test
    void testFindTaskForProcessId_multiple() {
        // set up the fixture
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<Task> tasks = Arrays.asList(task1, task2);

        TaskQuery taskQuery = mock(TaskQuery.class);
        when(taskQuery.processInstanceId(anyString())).thenReturn(taskQuery);
        when(taskQuery.includeProcessVariables()).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(tasks);

        TaskService taskService = mock(TaskService.class);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);

        ProcessIdToSingleTaskFunction flowableTaskUtilities = new ProcessIdToSingleTaskFunction(taskService);

        UUID processId = UUID.randomUUID();

        // execute the SUT
        IllegalStateException result = assertThrows(IllegalStateException.class, () -> {
            flowableTaskUtilities.apply(processId);
        });

        // validation
        assertThat(result.getMessage()).isEqualTo("Found more than one task for process ID: " + processId);
    }

    @Test
    void testFindTaskForProcessId_one() {
        // set up the fixture
        Task task = mock(Task.class);
        List<Task> tasks = Arrays.asList(task);

        TaskQuery taskQuery = mock(TaskQuery.class);
        when(taskQuery.processInstanceId(anyString())).thenReturn(taskQuery);
        when(taskQuery.includeProcessVariables()).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(tasks);

        TaskService taskService = mock(TaskService.class);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);

        ProcessIdToSingleTaskFunction flowableTaskUtilities = new ProcessIdToSingleTaskFunction(taskService);

        // execute the SUT
        var result = flowableTaskUtilities.apply(UUID.randomUUID());

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(task);
    }

    @Test
    void testFindTaskForProcessId_zero() {
        // set up the fixture
        Task task = mock(Task.class);
        List<Task> tasks = Collections.EMPTY_LIST;

        TaskQuery taskQuery = mock(TaskQuery.class);
        when(taskQuery.processInstanceId(anyString())).thenReturn(taskQuery);
        when(taskQuery.includeProcessVariables()).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(tasks);

        TaskService taskService = mock(TaskService.class);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);

        ProcessIdToSingleTaskFunction flowableTaskUtilities = new ProcessIdToSingleTaskFunction(taskService);

        // execute the SUT
        var result = flowableTaskUtilities.apply(UUID.randomUUID());

        // validation
        assertThat(result).isEmpty();
    }

}
