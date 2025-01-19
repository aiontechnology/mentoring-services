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

package io.aiontechnology.mentoring.api.error;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * Model for errors produced by the server.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
@Data
@Builder(setterPrefix = "with")
@ToString
public class ErrorModel<T> {

    /** The timestamp of when the error occurred. */
    private final ZonedDateTime timestamp;

    /** The HTTP status that resulted from the error. */
    private final HttpStatus status;

    /** The error. */
    private final T error;

    /** The error message. */
    private final String message;

    /** The path that caused the error. */
    private final String path;

}
