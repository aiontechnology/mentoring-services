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

import io.aiontechnology.mentoring.model.inbound.student.InboundStudent;
import io.aiontechnology.mentoring.model.outbound.student.OutboundStudent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Client for the student API.
 *
 * @author Whitney Hunter
 * @since 0.4.0
 */
@FeignClient("students")
public interface StudentApiClient {

    /**
     * Send a POST to create a new student.
     *
     * @param schoolId The id of the school for the new student.
     * @param student The new student.
     * @return The created student.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/schools/{schoolId}/students")
    OutboundStudent createStudent(@PathVariable("schoolId") UUID schoolId, InboundStudent student);

    /**
     * Send a GET to retrieve all students for a school.
     *
     * @param schoolId The id of the school.
     * @return A collection of students for the school.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/schools/{schoolId}")
    CollectionModel<OutboundStudent> getAllStudents(@PathVariable("schoolId") UUID schoolId);

    /**
     * Send a GET to retrieve a single student for a school.
     *
     * @param schoolId The id of the school.
     * @param studentId The id of the desired student.
     * @return The student.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/schools/{schoolId}/students/{studentId}")
    OutboundStudent getStudent(@PathVariable("schoolId") UUID schoolId, @PathVariable("studentId") UUID studentId);

    /**
     * Send a PUT to update a student.
     *
     * @param schoolId The id of the school.
     * @param studentId The id of the student to update.
     * @param student The student model to update to.
     * @return The updated student.
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/schools/{schoolId}/students/{studentId}")
    OutboundStudent updateStudent(@PathVariable("schoolId") UUID schoolId, @PathVariable("studentId") UUID studentId,
            InboundStudent student);

    /**
     * Send a DELETE to deactivate a student.
     *
     * @param schoolId The id of the school.
     * @param studentId The id of the student to deactivate.
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/schools/{schoolId}/students/{studentId}")
    void deleteStudent(@PathVariable("schoolId") UUID schoolId, @PathVariable("studentId") UUID studentId);

}
