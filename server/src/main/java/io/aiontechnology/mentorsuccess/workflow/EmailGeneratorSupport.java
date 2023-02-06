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

package io.aiontechnology.mentorsuccess.workflow;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Map;

import static io.aiontechnology.mentorsuccess.workflow.RegistrationWorkflowConstants.EMAIL;

public abstract class EmailGeneratorSupport implements JavaDelegate {

    @Override
    public final void execute(DelegateExecution execution) {
        execution.setVariable(EMAIL, createEmailConfiguration(execution));
        setAdditionalVariables(execution);
    }

    protected abstract String getBody(DelegateExecution execution);

    protected abstract String getFrom(DelegateExecution execution);

    protected abstract String getSubject(DelegateExecution execution);

    protected abstract String getTo(DelegateExecution execution);

    protected void setAdditionalVariables(DelegateExecution execution) {
    }

    private Map<String, Object> createEmailConfiguration(DelegateExecution execution) {
        return Map.of(
                "subject", getSubject(execution),
                "to", getTo(execution),
                "from", getFrom(execution),
                "body", getBody(execution)
        );
    }

}
