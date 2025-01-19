/*
 * Copyright 2022 Aion Technology LLC
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

package io.aiontechnology.mentoring.api.assembler.impl;

import io.aiontechnology.mentoring.api.assembler.ReflectionAssemblerSupport;
import io.aiontechnology.mentoring.api.controller.BookController;
import io.aiontechnology.mentoring.entity.Book;
import io.aiontechnology.mentoring.resource.BookResource;
import org.springframework.hateoas.Link;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author Whitney Hunter
 * @since 1.8.0
 */
public class BookAssembler extends ReflectionAssemblerSupport<Book, BookResource> {

    public BookAssembler() {
        super(BookResource.class);
    }

    @Override
    protected Set<Link> getLinks(BookResource model) {
        Book book = model.getContent();
        return Set.of(
                linkTo(BookController.class).slash(book.getId()).withSelfRel()
        );
    }

}
