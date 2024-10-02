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

package io.aiontechnology.mentoring.api.mapping.toentity.misc;

import io.aiontechnology.atlas.mapping.OneWayMapper;
import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Whitney Hunter
 * @since 1.1.0
 */
@Component
@RequiredArgsConstructor
public class UriModelToBookMapper implements OneWayMapper<URI, Book> {

    private final Pattern BOOK_PATTERN = Pattern.compile("https?://.*/api/v1/books/(.*)");

    private final BookService bookService;

    @Override
    public Optional<Book> map(URI bookUri) {
        return Optional.ofNullable(bookUri).
                flatMap(u -> {
                    UUID bookUUID = null;

                    Matcher matcher1 = BOOK_PATTERN.matcher(bookUri.toString());
                    if (matcher1.find()) {
                        bookUUID = UUID.fromString(matcher1.group(1));
                    }

                    return bookUUID != null ? bookService.findBookById(bookUUID) : Optional.empty();
                });
    }

}
