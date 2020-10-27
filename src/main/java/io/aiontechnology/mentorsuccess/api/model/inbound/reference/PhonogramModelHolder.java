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

package io.aiontechnology.mentorsuccess.api.model.inbound.reference;

import io.aiontechnology.mentorsuccess.api.model.inbound.PhonogramModel;
import io.aiontechnology.mentorsuccess.entity.reference.Phonogram;

import java.util.Collection;

/**
 * Represents an object that contains a collection of {@link Phonogram} objects.
 *
 * @author Whitney Hunter
 * @since 0.1.0
 */
public interface PhonogramModelHolder {

    /**
     * Get the collection of {@link PhonogramModel} objects held by the holder.
     *
     * @return The collection {@link PhonogramModel} objects.
     */
    Collection<PhonogramModel> getPhonograms();

}