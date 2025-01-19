/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.assembler;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.impl.ReflectionCompoundOneWayMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class ReflectionAssemblerSupport<ENTITY, MODEL extends EntityModel>
        extends ReflectionCompoundOneWayMapper<ENTITY, MODEL> implements Assembler<ENTITY, MODEL> {

    protected ReflectionAssemblerSupport(Class<MODEL> modelClass) {
        super(modelClass);
    }

    @Override
    public Optional<MODEL> map(ENTITY from) {
        return Optional.ofNullable(from)
                .flatMap(super::map)
                .map(this::addLinks);
    }

    @Override
    public Optional<MODEL> mapWithData(ENTITY from, Map data) {
        return map(from);
    }

    public ReflectionAssemblerSupport<ENTITY, MODEL> withSubMapper(String key, OneWayMapper<?, ?> subMapper) {
        addSubMapper(key, subMapper);
        return this;
    }

    protected Set<Link> getLinks(MODEL model) {
        return Collections.emptySet();
    }

    private MODEL addLinks(MODEL model) {
        getLinks(model).spliterator().forEachRemaining(model::add);
        return model;
    }

}
