package io.aiontechnology.mentorsuccess.model.inbound;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder(setterPrefix = "with")
public class InboundPostAssessment {

    @NotNull
    String postAssessmentUri;

}
