/*
 * Copyright 2022-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.service;

import io.aiontechnology.mentoring.entity.School;
import io.aiontechnology.mentoring.entity.SchoolSession;
import io.aiontechnology.mentoring.entity.StudentSchoolSession;
import io.aiontechnology.mentoring.repository.SchoolRepository;
import io.aiontechnology.mentoring.repository.SchoolSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
@Service
@RequiredArgsConstructor
public class SchoolSessionService {

    private final SchoolRepository schoolRepository;
    private final SchoolSessionRepository schoolSessionRepository;

    public void deleteSchoolSession(SchoolSession schoolSession) {
        schoolSessionRepository.delete(schoolSession);
    }

    public Optional<SchoolSession> getSchoolSessionById(UUID id) {
        return schoolSessionRepository.findById(id);
    }

    @Transactional
    public SchoolSession saveSchoolSession(SchoolSession schoolSession) {
        schoolSessionRepository.save(schoolSession);
        return schoolSession;
    }

    @Transactional
    public SchoolSession updateSchoolSession(School school, SchoolSession newSchoolSession) {
        Optional.ofNullable(school.getCurrentSession())
                .ifPresent(currentSession -> {
                    Collection<StudentSchoolSession> newStudentSchoolSessions =
                            currentSession.getStudentSchoolSessions().stream()
                                    .map(StudentSchoolSession::new)
                                    .map(StudentSchoolSession::incrementGrade)
                                    .map(studentSchoolSession -> studentSchoolSession.setSession(newSchoolSession))
                                    .map(studentSchoolSession -> {
                                        studentSchoolSession.setIsRegistrationSigned(Boolean.TRUE);
                                        return studentSchoolSession;
                                    })
                                    .collect(Collectors.toList());

                    newSchoolSession.setStudentSchoolSessions(newStudentSchoolSessions);
                });

        school.setCurrentSession(newSchoolSession);
        newSchoolSession.setSchool(school);
        schoolRepository.save(school);

        return school.getCurrentSession();
    }

}
