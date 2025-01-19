package io.aiontechnology.mentoring.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UriBuilder {

    private String baseUri;
    private List<String> pathAdditions = new ArrayList<>();
    private Map<String, String> params = new LinkedHashMap<>();

    public UriBuilder(String baseUri) {
        this.baseUri = baseUri;
    }

    public String build() {
        String value = baseUri;
        for(String pathAddition: pathAdditions) {
            value += "/" + pathAddition;
        }
        boolean isFirst = true;
        for (String index : params.keySet()) {
            value += isFirst ? "?" : "&";
            value += index + "=" + params.get(index);
            isFirst = false;
        }
        return value;
    }

    public UriBuilder withPathAddition(String addition) {
        pathAdditions.add(addition);
        return this;
    }

    public UriBuilder withParam(String param, String value) {
        if (value != null) {
            params.put(param, value);
        }
        return this;
    }

}
