package io.aiontechnology.mentorsuccess.workflow;

import io.aiontechnology.mentorsuccess.entity.workflow.StudentRegistration;
import io.aiontechnology.mentorsuccess.model.inbound.InboundInvitation;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.TaskService;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.INVITATION;

@Service
@RequiredArgsConstructor
public class StudentRegistrationWorkflowService {

    // Services
    private final TaskService taskService;

    public Optional<StudentRegistration> findStudentRegistrationWorkflowById(UUID processId) {
        StudentRegistration studentRegistration = new StudentRegistration();
        TaskQuery query = taskService.createTaskQuery()
                .processInstanceId(processId.toString())
                .includeProcessVariables();
        return query.list().stream()
                .findFirst()
                .map(task -> {
                    studentRegistration.setId(UUID.fromString(task.getProcessInstanceId()));
                    return task;
                })
                .map(TaskInfo::getProcessVariables)
                .map(variables -> {
                    InboundInvitation inboundInvitation = (InboundInvitation) variables.get(INVITATION);
                    studentRegistration.setStudentFirstName(inboundInvitation.getStudentFirstName());
                    studentRegistration.setStudentLastName(inboundInvitation.getStudentLastName());
                    studentRegistration.setParent1FirstName(inboundInvitation.getParent1FirstName());
                    studentRegistration.setParent1LastName(inboundInvitation.getParent1LastName());
                    studentRegistration.setParent1EmailAddress(inboundInvitation.getParent1EmailAddress());
                    return studentRegistration;
                });
    }

}
