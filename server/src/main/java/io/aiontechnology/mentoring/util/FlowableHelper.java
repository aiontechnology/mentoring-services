package io.aiontechnology.mentoring.util;

import org.flowable.engine.delegate.DelegateExecution;

import java.util.Map;

public class FlowableHelper {

    public static String getVariableAsString(DelegateExecution execution, String key) {
        Map<String, Object> variables = execution.getVariables();
        return getVariableAsString(variables, key);
    }

    public static String getVariableAsString(Map<String, Object> variables, String key) {
        return (String) variables.get(key);
    }

}
