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

package io.aiontechnology.mentoring.velocity;

import io.aiontechnology.mentoring.model.inbound.student.InboundStudentInformation;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;

public class TeacherInvitationCompleteEmailGenerator extends VelocityGenerationStrategySupport {

    private static final String TEMPLATE_NAME = "templates/teacher/information-request-complete-email.vm";

    public String render(String programAdminName, String studentName, InboundStudentInformation studentInformation) {
        VelocityContext context = createContext(programAdminName, studentName, studentInformation);
        StringWriter writer = new StringWriter();

        Velocity.getTemplate(TEMPLATE_NAME).merge(context, writer);
        return writer.toString();
    }

    private VelocityContext createContext(String programAdminName, String studentName,
            InboundStudentInformation studentInformation) {
        VelocityContext context = new VelocityContext();
        context.put("programAdminName", programAdminName);
        context.put("studentName", studentName);
        context.put("studentInformation", studentInformation);
        context.put("behaviors", String.join(", ", studentInformation.getBehaviors()));
        context.put("leadershipSkills", String.join(", ", studentInformation.getLeadershipSkills()));
        context.put("leadershipTraits", String.join(", ", studentInformation.getLeadershipTraits()));
        return context;
    }

}
