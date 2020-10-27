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

package io.aiontechnology.mentorsuccess.api.assembler;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

/**
 * @author Whitney Hunter
 * @since 0.3.0
 */
public abstract class LinkAddingRepresentationModelAssemblerSupport<FROM, TO extends RepresentationModel<TO>>
        extends RepresentationModelAssemblerSupport<FROM, TO> {

    @Getter
    private final LinkHelper<TO> linkHelper;

    LinkAddingRepresentationModelAssemblerSupport(Class<?> controllerClass, Class<TO> resourceType,
            LinkHelper<TO> linkHelper) {
        super(controllerClass, resourceType);
        this.linkHelper = linkHelper;
    }

    public abstract TO toModel(FROM from, LinkProvider<TO, FROM> linkProvider);

}