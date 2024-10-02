/*
 * Copyright 2020 Aion Technology LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.aiontechnology.mentoring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import static org.springframework.hateoas.mediatype.hal.HalConfiguration.RenderSingleLinks;

/**
 * Configuration for the REST interface.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Configuration
public class RestConfiguration {

    /**
     * Create a {@link HalConfiguration} bean.
     *
     * @return The {@link HalConfiguration}.
     */
    @Bean
    public HalConfiguration patternBasedPolicy() {
        return new HalConfiguration()
                .withRenderSingleLinks(RenderSingleLinks.AS_ARRAY);
    }

    /**
     * Create a {@link ForwardedHeaderFilter} bean.
     *
     * @return The {@link ForwardedHeaderFilter}.
     */
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    /**
     * Create a {@link ForwardedHeaderFilter} bean.
     *
     * @return The {@link ForwardedHeaderFilter}.
     */
    @Bean
    public ForwardedHeaderTransformer forwardedHeaderTransformer() {
        return new ForwardedHeaderTransformer();
    }

}
