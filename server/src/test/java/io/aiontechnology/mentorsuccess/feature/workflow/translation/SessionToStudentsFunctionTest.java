/*
 * Copyright 2024 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.feature.workflow.translation;

import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.entity.StudentSchoolSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionToStudentsFunctionTest {

    @Test
    void testFunction_AllActive() {
        // set up the fixture
        var student1 = new Student();
        var studentSchoolSession1 = new StudentSchoolSession();
        studentSchoolSession1.setIsActive(true);
        studentSchoolSession1.setStudent(student1);

        var student2 = new Student();
        var studentSchoolSession2 = new StudentSchoolSession();
        studentSchoolSession2.setIsActive(true);
        studentSchoolSession2.setStudent(student2);

        SchoolSession schoolSession = new SchoolSession();
        schoolSession.setStudentSchoolSessions(Arrays.asList(studentSchoolSession1, studentSchoolSession2));

        // execute the SUT
        var result = new SessionToStudentsFunction().apply(schoolSession);

        // validation
        assertThat(result).containsExactly(student1, student2);
    }

    @Test
    void testFunction_SomeNotActive() {
        // set up the fixture
        var student1 = new Student();
        var studentSchoolSession1 = new StudentSchoolSession();
        studentSchoolSession1.setIsActive(false);
        studentSchoolSession1.setStudent(student1);

        var student2 = new Student();
        var studentSchoolSession2 = new StudentSchoolSession();
        studentSchoolSession2.setIsActive(true);
        studentSchoolSession2.setStudent(student2);

        SchoolSession schoolSession = new SchoolSession();
        schoolSession.setStudentSchoolSessions(Arrays.asList(studentSchoolSession1, studentSchoolSession2));

        // execute the SUT
        var result = new SessionToStudentsFunction().apply(schoolSession);

        // validation
        assertThat(result).containsExactly(student2);
    }

}
