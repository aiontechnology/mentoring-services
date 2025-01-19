/*
 * Copyright 2020 Aion Technology LLC
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

package io.aiontechnology.mentoring.client.feign;

import io.aiontechnology.mentoring.model.inbound.InboundBook;
import io.aiontechnology.mentoring.model.outbound.OutboundBook;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Whitney Hunter
 * @since 0.4.0
 */
@FeignClient("books")
public interface BookApiClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/books")
    OutboundBook createBook(InboundBook book);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/books")
    CollectionModel<OutboundBook> getAllBooks();

}
