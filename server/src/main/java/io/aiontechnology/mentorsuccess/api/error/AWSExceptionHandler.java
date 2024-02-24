/*
 * Copyright 2024 Aion Technology LLC
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

package io.aiontechnology.mentorsuccess.api.error;

import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class AWSExceptionHandler {

    @ExceptionHandler(AWSCognitoIdentityProviderException.class)
    public ResponseEntity<ErrorModel> handleIdentityProviderException(AWSCognitoIdentityProviderException exception,
            HttpServletRequest request) {
        ErrorModel<Map<String, String>> errorModel = ErrorModel.<Map<String, String>>builder()
                .withTimestamp(ZonedDateTime.now(ZoneOffset.UTC))
                .withStatus(HttpStatus.BAD_REQUEST)
                .withMessage(exception.getErrorMessage())
                .withPath(request.getRequestURI())
                .build();
        log.debug("Error {}", errorModel);
        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

}
