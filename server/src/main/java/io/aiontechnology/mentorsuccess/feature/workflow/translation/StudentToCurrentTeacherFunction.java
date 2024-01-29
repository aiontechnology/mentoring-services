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

import io.aiontechnology.mentorsuccess.entity.SchoolPersonRole;
import io.aiontechnology.mentorsuccess.entity.SchoolSession;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.entity.StudentSchoolSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class StudentToCurrentTeacherFunction implements
        Function<Pair<Optional<Student>, Optional<SchoolSession>>, Optional<SchoolPersonRole>> {

    @Override
    public Optional<SchoolPersonRole> apply(Pair<Optional<Student>, Optional<SchoolSession>> input) {
        return input.getLeft().flatMap(s1 ->
                input.getRight().flatMap(s2 ->
                        s1.findCurrentSessionForStudent(s2)
                                .map(StudentSchoolSession::getTeacher)
                )
        );
    }

}
