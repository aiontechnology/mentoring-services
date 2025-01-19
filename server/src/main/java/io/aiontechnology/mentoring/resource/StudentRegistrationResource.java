package io.aiontechnology.mentoring.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.aiontechnology.mentoring.entity.workflow.StudentRegistration;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudentRegistration;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"content"})
public class StudentRegistrationResource extends OutboundStudentRegistration<StudentRegistration> {

    public StudentRegistrationResource(StudentRegistration studentRegistration) {
        super(studentRegistration);
    }

}
