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

import io.aiontechnology.mentoring.model.inbound.student.InboundStudentRegistration;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;

public class RegistrationCompleteEmailGenerator extends VelocityGenerationStrategySupport {

    private static final String TEMPLATE_NAME = "templates/student/registration-complete-email.vm";

    public String render(String programAdminName, String teacherName, InboundStudentRegistration registration) {
        VelocityContext context = createContext(programAdminName, teacherName, registration);
        StringWriter writer = new StringWriter();

        Velocity.getTemplate(TEMPLATE_NAME).merge(context, writer);
        return writer.toString();
    }

    private VelocityContext createContext(String programAdminName, String teacherName,
            InboundStudentRegistration registration) {
        VelocityContext context = new VelocityContext();
        context.put("programAdminName", programAdminName);
        context.put("teacherName", teacherName);
        context.put("registration", registration);
        return context;
    }

}
