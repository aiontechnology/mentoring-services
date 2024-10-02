/*
 * Copyright 2020-2024 Aion Technology LLC
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

package io.aiontechnology.mentoring.model.inbound;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

/**
 * A model that represents an inbound address.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundAddress {

    /** The first street address. */
    @Size(max = 50, message = "{address.street1.size}")
    String street1;

    /** The second street address. */
    @Size(max = 50, message = "{address.street2.size}")
    String street2;

    /** The city. */
    @Size(max = 50, message = "{address.city.size}")
    String city;

    /** The state. */
    @Size(min = 2, max = 2, message = "{address.state.size}")
    String state;

    /** The zip code */
    @Size(min = 5, max = 9, message = "{address.zip.size}")
    String zip;

}
