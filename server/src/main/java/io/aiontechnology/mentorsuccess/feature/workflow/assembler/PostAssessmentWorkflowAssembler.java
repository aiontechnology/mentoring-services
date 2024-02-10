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

package io.aiontechnology.mentorsuccess.feature.workflow.assembler;

import io.aiontechnology.mentorsuccess.api.assembler.AssemblerSupport;
import io.aiontechnology.mentorsuccess.entity.School;
import io.aiontechnology.mentorsuccess.entity.Student;
import io.aiontechnology.mentorsuccess.feature.workflow.controller.PostAssessmentWorkflowLookupController;
import io.aiontechnology.mentorsuccess.feature.workflow.model.PostAssessmentWorkflow;
import io.aiontechnology.mentorsuccess.resource.PostAssessmentWorkflowResource;
import org.springframework.hateoas.Link;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.POST_ASSESSMENT;
import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.SCHOOL;
import static io.aiontechnology.mentorsuccess.feature.workflow.WorkflowKeys.STUDENT;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PostAssessmentWorkflowAssembler extends AssemblerSupport<PostAssessmentWorkflow,
        PostAssessmentWorkflowResource> {
    @Override
    protected Optional<PostAssessmentWorkflowResource> doMapWithData(PostAssessmentWorkflow postAssessmentWorkflow,
            Map data) {
        return Optional.ofNullable(postAssessmentWorkflow).map(workflow -> {
            PostAssessmentWorkflowResource resource = new PostAssessmentWorkflowResource(workflow);
            return resource;
        });
    }

    @Override
    protected Set<Link> getLinks(PostAssessmentWorkflowResource model, Map data) {
        School school = (School) data.get(SCHOOL);
        Student student = (Student) data.get(STUDENT);
        UUID workflowId = (UUID) data.get(POST_ASSESSMENT);

        return Set.of(
                linkTo(PostAssessmentWorkflowLookupController.class, school.getId(), student.getId())
                        .slash(workflowId.toString())
                        .withSelfRel()
        );
    }

}
