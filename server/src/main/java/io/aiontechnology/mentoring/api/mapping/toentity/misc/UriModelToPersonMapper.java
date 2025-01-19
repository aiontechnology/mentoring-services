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

package io.aiontechnology.mentoring.api.mapping.toentity.misc;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.Person;
import io.aiontechnology.mentoring.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A mapper that converts a {@link URI} to a {@link Person}.
 *
 * @author Whitney Hunter
 * @since 0.3.0
 */
@Component
@RequiredArgsConstructor
public class UriModelToPersonMapper implements OneWayMapper<URI, Person> {

    private final Pattern PERSON_PATTERN =
            Pattern.compile("https?://.*/api/v1/people/(.*)");

    private final PersonService personService;

    /**
     * Map the given {@link URI} to a {@link Person}.
     *
     * @param personUri The {@link URI} to map.
     * @return The resulting {@link Person}.
     */
    @Override
    public Optional<Person> map(URI personUri) {
        return Optional.ofNullable(personUri).
                flatMap(u -> {
                    UUID personUUID = null;

                    Matcher matcher1 = PERSON_PATTERN.matcher(personUri.toString());
                    if (matcher1.find()) {
                        personUUID = UUID.fromString(matcher1.group(1));
                    }

                    return personUUID != null ? personService.findPersonById(personUUID) : Optional.empty();
                });
    }

}
