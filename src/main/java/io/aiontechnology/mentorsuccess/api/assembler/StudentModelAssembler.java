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

package io.aiontechnology.mentorsuccess.api.assembler;

import io.aiontechnology.mentorsuccess.api.controller.StudentController;
import io.aiontechnology.mentorsuccess.api.mapping.OneWayMapper;
import io.aiontechnology.mentorsuccess.api.mapping.tomodel.student.StudentEntityToModelMapper;
import io.aiontechnology.mentorsuccess.api.model.outbound.student.OutboundStudentModel;
import io.aiontechnology.mentorsuccess.entity.Student;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Assembles a {@link OutboundStudentModel} from a {@link Student}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
public class StudentModelAssembler extends RepresentationModelAssemblerSupport<Student, OutboundStudentModel> {

    private final OneWayMapper<Student, OutboundStudentModel> entityToModelMapper;

    /** A utility class for adding links to a model object. */
    private final LinkHelper<OutboundStudentModel> linkHelper;

    @Inject
    public StudentModelAssembler(StudentEntityToModelMapper entityToModelMapper,
            LinkHelper<OutboundStudentModel> linkHelper) {
        super(StudentController.class, OutboundStudentModel.class);
        this.entityToModelMapper = entityToModelMapper;
        this.linkHelper = linkHelper;
    }

    @Override
    public OutboundStudentModel toModel(Student student) {
        return Optional.ofNullable(student)
                .map(entityToModelMapper::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);
    }

    public OutboundStudentModel toModel(Student student, LinkProvider<OutboundStudentModel, Student> linkProvider) {
        return Optional.ofNullable(student)
                .map(this::toModel)
                .map(model -> linkHelper.addLinks(model, linkProvider.apply(model, student)))
                .orElse(null);
    }

}