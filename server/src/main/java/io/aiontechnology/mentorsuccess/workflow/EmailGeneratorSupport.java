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

package io.aiontechnology.mentorsuccess.workflow;

import io.aiontechnology.mentorsuccess.util.SAEmailAddress;
import io.aiontechnology.mentorsuccess.velocity.VelocityGenerationStrategy;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Map;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.EMAIL;

public abstract class EmailGeneratorSupport implements JavaDelegate {

    public static final String DEFAULT_FROM_EMAIL_ADDRESS = "The Live Your Dreams Foundation™ <admin@inspiresuccess.co>";

    private Expression generationStrategyClassName;

    private TaskUtilities taskUtilities;

    protected EmailGeneratorSupport(TaskUtilities taskUtilities) {
        this.taskUtilities = taskUtilities;
    }

    @Override
    public final void execute(DelegateExecution execution) {
        execution.setTransientVariable(EMAIL, createEmailConfiguration(execution));
        setAdditionalVariables(execution);
    }

    protected abstract String getBody(DelegateExecution execution);

    protected String getCC(DelegateExecution execution) {
        var schoolEmailTag = taskUtilities.getSchoolEmailTag(execution);
        return SAEmailAddress.builder()
                .withTag(schoolEmailTag)
                .build()
                .toString();
    }

    protected abstract String getFrom(DelegateExecution execution);

    protected <T extends VelocityGenerationStrategy> T getGenerationStrategy(DelegateExecution execution,
            Class<T> clazz) {
        String name = (String) generationStrategyClassName.getValue(execution);
        try {
            Class generationClass = Class.forName(name);
            return clazz.cast(generationClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract String getSubject(DelegateExecution execution);

    protected TaskUtilities getTaskUtilities() {
        return taskUtilities;
    }

    protected abstract String getTo(DelegateExecution execution);

    protected void setAdditionalVariables(DelegateExecution execution) {
    }

    private Map<String, Object> createEmailConfiguration(DelegateExecution execution) {
        return Map.of(
                "subject", getSubject(execution),
                "to", getTo(execution),
                "cc", getCC(execution),
                "from", getFrom(execution),
                "body", getBody(execution)
        );
    }

}
