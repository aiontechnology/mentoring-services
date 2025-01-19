package io.aiontechnology.mentoring.workflow;

import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.Student;
import io.aiontechnology.mentoring.service.SchoolService;
import io.aiontechnology.mentoring.service.StudentService;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.SCHOOL_ID;
import static io.aiontechnology.mentoring.workflow.RegistrationWorkflowConstants.STUDENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowableProcessUtilitiesTest {

    @Test
    void testFindStudent() {
        // set up the fixture
        UUID schoolId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        SchoolSession schoolSession = mock(SchoolSession.class);

        School school = mock(School.class);
        when(school.getCurrentSession()).thenReturn(schoolSession);

        Student student = mock(Student.class);

        SchoolService schoolService = mock(SchoolService.class);
        when(schoolService.getSchoolById(schoolId)).thenReturn(Optional.of(school));

        StudentService studentService = mock(StudentService.class);
        when(studentService.getStudentById(studentId, schoolSession)).thenReturn(Optional.of(student));

        FlowableProcessUtilities flowableProcessUtilities = new FlowableProcessUtilities(schoolService, studentService);

        Map<String, Object> processVariables = Map.of(SCHOOL_ID, schoolId.toString(), STUDENT_ID, studentId.toString());

        // execute the SUT
        var result = flowableProcessUtilities.findStudent(processVariables);

        // validation
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(student);
    }

    /**
     * Test that a {@link NotFoundException} is thrown when no school can be found from the ID.
     */
    @Test
    void testFindStudentNoSchool() {
        // set up the fixture
        UUID schoolId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        SchoolSession schoolSession = mock(SchoolSession.class);

        SchoolService schoolService = mock(SchoolService.class);
        when(schoolService.getSchoolById(schoolId)).thenReturn(Optional.empty());

        StudentService studentService = mock(StudentService.class);

        FlowableProcessUtilities flowableProcessUtilities = new FlowableProcessUtilities(schoolService, studentService);

        Map<String, Object> processVariables = Map.of(SCHOOL_ID, schoolId.toString(), STUDENT_ID, studentId.toString());

        // execute the SUT
        NotFoundException result = assertThrows(NotFoundException.class, () -> {
            flowableProcessUtilities.findStudent(processVariables);
        });

        // validation
        assertThat(result.getMessage()).isEqualTo("School was not found");
    }

}
