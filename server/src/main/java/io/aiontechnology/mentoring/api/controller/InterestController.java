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

package io.aiontechnology.mentoring.api.controller;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.atlas.mapping.OneWayUpdateMapper;
import io.aiontechnology.mentoring.api.assembler.Assembler;
import io.aiontechnology.mentoring.api.error.NotFoundException;
import io.aiontechnology.mentoring.entity.reference.Interest;
import io.aiontechnology.mentoring.model.inbound.InboundInterest;
import io.aiontechnology.mentoring.resource.InterestResource;
import io.aiontechnology.mentoring.service.InterestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller that vends a REST interface for dealing with interests.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@RestController
@RequestMapping("/api/v1/interests")
@RequiredArgsConstructor
@Slf4j
public class InterestController {

    // Assemblers
    private final Assembler<Interest, InterestResource> interestAssembler;

    // Mappers
    private final OneWayMapper<InboundInterest, Interest> interestMapper =
            inboundInterest -> Optional.of(new Interest())
                    .map(i -> {
                        i.setName(inboundInterest.getName());
                        return i;
                    });

    private final OneWayUpdateMapper<InboundInterest, Interest> interestUpdateMapper =
            (inboundInterest, interest) -> Optional.ofNullable(interest)
                    .map(i -> {
                        i.setName(inboundInterest.getName());
                        return i;
                    });

    // Services
    private final InterestService interestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('resources:update')")
    public InterestResource createInterest(@RequestBody @Valid InboundInterest inboundInterest) {
        return Optional.ofNullable(inboundInterest)
                .flatMap(interestMapper::map)
                .map(interestService::createInterest)
                .flatMap(interestAssembler::map)
                .orElseThrow(() -> new IllegalStateException("Unexpected error"));
    }

    @GetMapping("/{interestId}")
    @PreAuthorize("hasAuthority('resource:read')")
    public InterestResource getInterest(@PathVariable("interestId") UUID interestId) {
        return interestService.findInterestById(interestId)
                .flatMap(interestAssembler::map)
                .orElseThrow(() -> new NotFoundException("Interest not found"));
    }

    /**
     * A REST endpoint for getting all interests.
     *
     * @return A collection of models that represents the interests in the system.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('resource:read')")
    public CollectionModel<InterestResource> getInterests() {
        var interestModels = StreamSupport
                .stream(interestService.getAllInterests().spliterator(), false)
                .map(interestAssembler::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return CollectionModel.of(interestModels);
    }

    @PutMapping("/{interestId}")
    @PreAuthorize("hasAuthority('resources:update')")
    public InterestResource updateInterest(@PathVariable("interestId") UUID interestId,
            @RequestBody @Valid InboundInterest inboundInterest) {
        return interestService.findInterestById(interestId)
                .flatMap(interest -> interestUpdateMapper.map(inboundInterest, interest))
                .map(interestService::updateInterest)
                .flatMap(interestAssembler::map)
                .orElseThrow(() -> new IllegalArgumentException("Unable to update interest"));
    }

}
