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

package io.aiontechnology.mentorsuccess.feature.workflow.translation;

import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.entity.StudentSchoolSession;
import io.aiontechnology.mentorsuccess.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class IdAndSessionToStudent implements BiFunction<UUID, Optional<SchoolSession>, Optional<Student>> {

    private final StudentRepository studentRepository;

    @Override
    public Optional<Student> apply(UUID studentId, Optional<SchoolSession> currentSession) {
        return findActiveStudent(studentId, currentSession.orElseThrow());
    }

    private Optional<Student> findActiveStudent(UUID studentId, SchoolSession currentSession) {
        return studentRepository.findById(studentId)
                .filter(student -> isActiveStudent(student, currentSession));
    }

    private boolean isActiveStudent(Student student, SchoolSession session) {
        return student.findCurrentSessionForStudent(session)
                .map(StudentSchoolSession::getIsActive)
                .orElse(false);
    }

}
