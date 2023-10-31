package io.aiontechnology.mentorsuccess.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;

public abstract class TeacherEmailGeneratorSupport extends VelocityGenerationStrategySupport {

    public String render(String teacherFirstName, String studentName, String programAdminName, String programAdminEmail,
            String studentInfoUri, String studentAssessmentUri) {
        VelocityContext context = createContext(teacherFirstName, studentName, programAdminName, programAdminEmail,
                studentInfoUri, studentAssessmentUri);
        StringWriter writer = new StringWriter();

        Velocity.getTemplate(getTemplateName()).merge(context, writer);
        return writer.toString();
    }

    protected abstract String getTemplateName();

    private VelocityContext createContext(String teacherFirstName, String studentName, String programAdminName,
            String programAdminEmail, String studentInfoUri, String studentAssessmentUri) {
        VelocityContext context = new VelocityContext();
        context.put("teacherFirstName", teacherFirstName);
        context.put("studentName", studentName);
        context.put("programAdminName", programAdminName);
        context.put("programAdminEmail", programAdminEmail);
        context.put("studentInfoUri", studentInfoUri);
        context.put("studentAssessmentUri", studentAssessmentUri);
        return context;
    }

}
