/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentorsuccess.service;

import io.aiontechnology.mentorsuccess.entity.Teacher;
import io.aiontechnology.mentorsuccess.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing teachers.
 *
 * @author Whitney Hunter
 */
@Service
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher deactivateTeacher(Teacher teacher) {
        teacher.setIsActive(false);
        teacherRepository.save(teacher);
        return teacher;
    }

    public Optional<Teacher> findTeacher(UUID id) {
        return teacherRepository.findById(id);
    }

}
