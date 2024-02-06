/*
 * Copyright 2023-2024 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.velocity;

import io.aiontechnology.mentorsuccess.util.PhoneService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Optional;

public class StudentInvitationEmailGenerator extends VelocityGenerationStrategySupport {

    private static final String TEMPLATE_NAME = "templates/student/invitation-email.vm";

    private final PhoneService phoneService = new PhoneService();

    public String render(String parentFirstName, String schoolName, String programAdminName, String programAdminEmail,
            Optional<String> programAdminPhone, String registrationUri) {
        VelocityContext context = createContext(parentFirstName, schoolName, programAdminName, programAdminEmail,
                programAdminPhone, registrationUri);
        StringWriter writer = new StringWriter();

        Velocity.getTemplate(TEMPLATE_NAME).merge(context, writer);
        return writer.toString();
    }

    private VelocityContext createContext(String parentFirstName, String schoolName, String programAdminName,
            String programAdminEmail, Optional<String> programAdminPhone, String registrationUri) {
        VelocityContext context = new VelocityContext();
        context.put("parentFirstName", parentFirstName);
        context.put("schoolName", schoolName);
        context.put("programAdminName", programAdminName);
        context.put("programAdminEmail", programAdminEmail);
        context.put("programAdminPhone", phoneService.format(programAdminPhone));
        context.put("registrationUri", registrationUri);
        return context;
    }

}
