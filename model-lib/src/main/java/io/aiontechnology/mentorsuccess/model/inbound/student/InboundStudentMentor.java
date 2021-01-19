/*
 * Copyright 2020-2021 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.model.inbound.student;

import io.aiontechnology.mentorsuccess.model.enumeration.ResourceLocation;
import io.aiontechnology.mentorsuccess.model.validation.EnumNamePattern;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URI;

/**
 * @author Whitney Hunter
 * @since 0.6.0
 */
@Value
@Builder(setterPrefix = "with")
public class InboundStudentMentor {

    URI uri;

    @Size(max = 30, message = "{studentmentor.meetingTime.size}")
    String time;

    @NotNull(message = "{studentmentor.location.notNull}")
    @EnumNamePattern(regexp = "ONLINE|OFFLINE|BOTH", message = "{studentmentor.location.invalid}")
    ResourceLocation location;

    @NotNull(message = "{studentmentor.mediaRelease.notNull}")
    Boolean mediaReleaseSigned;

}
