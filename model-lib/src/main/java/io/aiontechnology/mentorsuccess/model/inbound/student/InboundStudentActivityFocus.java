/*
 * Copyright 2023 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.model.inbound.student;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Set;

@Value
@Builder(setterPrefix = "with")
public class InboundStudentActivityFocus {

    /** The associated activity focuses */
    @NotNull(message = "{studentleadershipskill.leadershipskill.notNull}")
    Set<String> activityFocuses;

    /** The URI of the associated person */
    @NotNull(message = "{studentleadershipskill.teacher.notNull}")
    URI teacher;

}
