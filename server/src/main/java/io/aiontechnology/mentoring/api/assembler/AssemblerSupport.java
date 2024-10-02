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
import io.aiontechnology.atlas.mapping.impl.CompoundOneWayMapper;
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
public abstract class AssemblerSupport<ENTITY, MODEL extends EntityModel<ENTITY>>
        extends CompoundOneWayMapper<ENTITY, MODEL> implements Assembler<ENTITY, MODEL> {

    @Override
    public final Optional<MODEL> map(ENTITY entity) {
        return Optional.ofNullable(entity)
                .flatMap(this::doMap)
                .map(this::addLinks);
    }

    @Override
    public Optional<MODEL> mapWithData(ENTITY entity, Map data) {
        return Optional.ofNullable(entity)
                .flatMap(e -> doMapWithData(e, data)
                        .map(this::addLinks)
                        .map(model -> addLinks(model, data)));
    }

    public AssemblerSupport<ENTITY, MODEL> withSubMapper(String key, OneWayMapper<?, ?> subMapper) {
        addSubMapper(key, subMapper);
        return this;
    }

    protected MODEL addLinks(MODEL model) {
        getLinks(model).spliterator()
                .forEachRemaining(model::add);
        return model;
    }

    protected MODEL addLinks(MODEL model, Map data) {
        getLinks(model, data).spliterator()
                .forEachRemaining(model::add);
        return model;
    }

    protected Optional<MODEL> doMap(ENTITY entity) {
        return Optional.empty();
    }

    protected Optional<MODEL> doMapWithData(ENTITY entity, Map data) {
        return Optional.empty();
    }

    protected Set<Link> getLinks(MODEL model) {
        return Collections.emptySet();
    }

    protected Set<Link> getLinks(MODEL model, Map data) {
        return Collections.emptySet();
    }

}
