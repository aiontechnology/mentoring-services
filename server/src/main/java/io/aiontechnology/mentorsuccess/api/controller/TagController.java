/*
 * Copyright 2021 Aion Technology LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.aiontechnology.mentorsuccess.api.controller;

import io.aiontechnology.mentorsuccess.api.assembler.TagModelAssembler;
import io.aiontechnology.mentorsuccess.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller that vends a REST interface for dealing with tags.
 *
 * @author Whitney Hunter
 * @since 1.13.0
 */
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Slf4j
public class TagController {

    /** Assembler for creating tag instances */
    private final TagModelAssembler tagModelAssembler;

    /** Service with business logic for tags */
    private final TagService tagService;

    /**
     * A REST endpoint for retrieving all tags.
     *
     * @return A collection of tag strings.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('resource:read')")
    public CollectionModel<String> getTags() {
        var tagModels = StreamSupport.stream(tagService.getAllTags().spliterator(), false)
                .map(tagModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(tagModels);
    }

}
