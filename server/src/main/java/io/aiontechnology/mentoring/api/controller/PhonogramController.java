/*
 * Copyright 2020-2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.controller;

import io.aiontechnology.mentoring.api.assembler.NameableToStringModelMapper;
import io.aiontechnology.mentoring.entity.reference.Phonogram;
import io.aiontechnology.mentoring.service.PhonogramService;
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
 * Controller that vends a REST interface for dealing with phonograms.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/phonograms")
@RequiredArgsConstructor
@Slf4j
public class PhonogramController {

    /** A HATEOAS assembler for a phonogram. */
    private final NameableToStringModelMapper<Phonogram> phonogramModelAssembler;

    /** Service for interacting with {@link Phonogram Phonograms}. */
    private final PhonogramService phonogramService;

    /**
     * A REST endpoint for getting all phonograms.
     *
     * @return A collection of models that represents the phonograms in the system.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('resource:read')")
    public CollectionModel<String> getPhonograms() {
        var phonogramModels = StreamSupport.stream(phonogramService.getAllPhonograms().spliterator(), false)
                .map(phonogramModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(phonogramModels);
    }

}
