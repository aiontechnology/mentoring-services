package io.aiontechnology.mentorsuccess.velocity;

public class TeacherPostAssessmentEmailGenerator extends TeacherEmailGeneratorSupport {

    private static final String TEMPLATE_NAME = "templates/teacher/post-assessment-request-email.vm";

    @Override
    protected String getTemplateName() {
        return TEMPLATE_NAME;
    }

}
