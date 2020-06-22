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

package io.aiontechnology.mentorsuccess.api.controller;

import io.aiontechnology.mentorsuccess.api.assembler.LeadershipSkillModelAssembler;
import io.aiontechnology.mentorsuccess.api.model.LeadershipSkillModel;
import io.aiontechnology.mentorsuccess.service.LeadershipSkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author <a href="mailto:whitney@aiontechnology.io">Whitney Hunter</a>
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/leadership_skills")
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
@Slf4j
public class LeadershipSkillController {

    private final LeadershipSkillModelAssembler leadershipSkillModelAssembler;

    private final LeadershipSkillService leadershipSkillService;

    @GetMapping
    public CollectionModel<LeadershipSkillModel> getLeaderhipSkills() {
        List<LeadershipSkillModel> interestModels = StreamSupport.stream(leadershipSkillService.getAllLeadershipSkills().spliterator(), false)
                .map(leadershipSkillModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(interestModels);
    }

}
