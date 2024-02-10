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

package io.aiontechnology.mentorsuccess.feature.workflow.velocity;

import io.aiontechnology.mentorsuccess.model.inbound.student.InboundStudentAssessment;
import io.aiontechnology.mentorsuccess.velocity.VelocityGenerationStrategySupport;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;

public class PostAssessmentCompleteEmailGenerator extends VelocityGenerationStrategySupport {

    private static final String TEMPLATE_NAME = "templates/teacher/post-assessment-complete-email.vm";

    public String render(String programAdminName, String studentName, InboundStudentAssessment studentAssessment) {
        VelocityContext context = createContext(programAdminName, studentName, studentAssessment);
        StringWriter writer = new StringWriter();

        Velocity.getTemplate(TEMPLATE_NAME).merge(context, writer);
        return writer.toString();
    }

    private VelocityContext createContext(String programAdminName, String studentName,
            InboundStudentAssessment studentAssessment) {
        VelocityContext context = new VelocityContext();
        context.put("programAdminName", programAdminName);
        context.put("studentName", studentName);
        context.put("postAssessment", studentAssessment);
        return context;
    }

}
