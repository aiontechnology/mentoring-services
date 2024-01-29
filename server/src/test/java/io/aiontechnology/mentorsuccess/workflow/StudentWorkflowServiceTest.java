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

import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.feature.workflow.process.ProcessIdToSingleTaskFunction;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StudentWorkflowServiceTest {

    @Test
    void testFindStudentInformationWorkflowById() {
        // set up the fixture
        var processId = UUID.randomUUID();
        var processInstanceId = UUID.randomUUID();

        Task task = mock(Task.class);
        when(task.getProcessInstanceId()).thenReturn(processInstanceId.toString());

        Map<String, Object> processVariables = new HashMap<>();

        String studentName = "STUDENT_NAME";
        Student student = mock(Student.class);
        when(student.getFullName()).thenReturn(studentName);

        FlowableProcessUtilities flowableProcessUtilities = mock(FlowableProcessUtilities.class);
        when(flowableProcessUtilities.findStudent(processVariables)).thenReturn(Optional.of(student));

        ProcessIdToSingleTaskFunction flowableTaskUtilities = mock(ProcessIdToSingleTaskFunction.class);
        when(flowableTaskUtilities.apply(any())).thenReturn(Optional.of(task));

        StudentWorkflowService studentWorkflowService = new StudentWorkflowService(flowableProcessUtilities,
                flowableTaskUtilities);

        // execute the SUT
        var result = studentWorkflowService.findStudentInformationWorkflowById(processId);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(processInstanceId);
        assertThat(result.get().getStudentName()).isEqualTo(studentName);
    }

    @Test
    void testFindStudentInformationWorkflowById_invalidProcessId() {
        // set up the fixture
        var processId = UUID.randomUUID();
        FlowableProcessUtilities flowableProcessUtilities = mock(FlowableProcessUtilities.class);

        ProcessIdToSingleTaskFunction flowableTaskUtilities = mock(ProcessIdToSingleTaskFunction.class);
        when(flowableTaskUtilities.apply(any())).thenReturn(Optional.empty());

        StudentWorkflowService studentWorkflowService = new StudentWorkflowService(flowableProcessUtilities,
                flowableTaskUtilities);

        // execute the SUT
        var result = studentWorkflowService.findStudentInformationWorkflowById(processId);

        // validation
        assertThat(result).isEmpty();
    }

    @Test
    void testFindStudentInformationWorkflowById_noStudentFound() {
        // set up the fixture
        var processId = UUID.randomUUID();
        var processInstanceId = UUID.randomUUID();

        Task task = mock(Task.class);
        when(task.getProcessInstanceId()).thenReturn(processInstanceId.toString());

        Map<String, Object> processVariables = new HashMap<>();

        FlowableProcessUtilities flowableProcessUtilities = mock(FlowableProcessUtilities.class);
        when(flowableProcessUtilities.findStudent(processVariables)).thenReturn(Optional.empty());

        ProcessIdToSingleTaskFunction flowableTaskUtilities = mock(ProcessIdToSingleTaskFunction.class);
        when(flowableTaskUtilities.apply(any())).thenReturn(Optional.of(task));

        StudentWorkflowService studentWorkflowService = new StudentWorkflowService(flowableProcessUtilities,
                flowableTaskUtilities);

        // execute the SUT
        var result = studentWorkflowService.findStudentInformationWorkflowById(processId);

        // validation
        assertThat(result).isEmpty();
    }

}
