package io.aiontechnology.mentorsuccess.workflow;

import io.aiontechnology.mentorsuccess.api.error.NotFoundException;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.service.SchoolService;
import io.aiontechnology.mentorsuccess.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.STUDENT_ID;

/**
 * Utilities for interacting with Flowable processes.
 */
@Service
@RequiredArgsConstructor
public class FlowableProcessUtilities {

    // Services
    private final SchoolService schoolService;
    private final StudentService studentService;

    /**
     * Find the student that is represented by the SCHOOL_ID and STUDENT_ID variables.
     *
     * @param processVariables The set of variables taken from a process.
     * @return The Student if it could be found.
     */
    public Optional<Student> findStudent(Map<String, Object> processVariables) {
        var schoolId = UUID.fromString((String) processVariables.get(SCHOOL_ID));
        var studentId = UUID.fromString((String) processVariables.get(STUDENT_ID));
        var school = schoolService.getSchoolById(schoolId)
                .orElseThrow(() -> new NotFoundException("School was not found"));
        return studentService.getStudentById(studentId, school.getCurrentSession());
    }

}
