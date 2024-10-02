package io.aiontechnology.mentoring.model.outbound;

import lombok.Builder;
import lombok.Value;

/**
 * @author Whitney Hunter
 * @since 0.4.0
 */
@Value
@Builder(setterPrefix = "with")
public class OutboundAddress {

    /** The first street address. */
    String street1;

    /** The second street address. */
    String street2;

    /** The city. */
    String city;

    /** The state. */
    String state;

    /** The zip code */
    String zip;

}
