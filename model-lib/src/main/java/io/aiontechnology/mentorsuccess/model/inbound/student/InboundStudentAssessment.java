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

package io.aiontechnology.mentorsuccess.model.inbound.student;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder(setterPrefix = "with")
public class InboundStudentAssessment implements Serializable {

    @NotNull(message = "{assessment.question1.notNull}")
    Integer question1;

    @NotNull(message = "{assessment.question2.notNull}")
    Integer question2;

    @NotNull(message = "{assessment.question3.notNull}")
    Integer question3;

    @NotNull(message = "{assessment.question4.notNull}")
    Integer question4;

    @NotNull(message = "{assessment.question5.notNull}")
    Integer question5;

    @NotNull(message = "{assessment.question6.notNull}")
    Integer question6;

    @NotNull(message = "{assessment.question7.notNull}")
    Integer question7;

    @NotNull(message = "{assessment.question8.notNull}")
    Integer question8;

    @NotNull(message = "{assessment.question9.notNull}")
    Integer question9;

    @NotNull(message = "{assessment.question10.notNull}")
    int question10;

    @NotNull(message = "{assessment.question11.notNull}")
    Integer question11;

    @NotNull(message = "{assessment.question12.notNull}")
    Integer question12;

    @NotNull(message = "{assessment.question13.notNull}")
    int question13;

    @NotNull(message = "{assessment.question14.notNull}")
    Integer question14;

    @NotNull(message = "{assessment.question15.notNull}")
    Integer question15;

    @NotNull(message = "{assessment.question16.notNull}")
    Integer question16;

    @NotNull(message = "{assessment.question17.notNull}")
    Integer question17;

    @NotNull(message = "{assessment.question18.notNull}")
    Integer question18;

    @NotNull(message = "{assessment.question19.notNull}")
    Integer question19;

    @NotNull(message = "{assessment.question20.notNull}")
    Integer question20;

    @NotNull(message = "{assessment.question21.notNull}")
    Integer question21;

    @NotNull(message = "{assessment.question22.notNull}")
    Integer question22;

    @NotNull(message = "{assessment.question23.notNull}")
    Integer question23;

    @NotNull(message = "{assessment.question24.notNull}")
    Integer question24;

    @NotNull(message = "{assessment.question25.notNull}")
    Integer question25;

    @NotNull(message = "{assessment.question26.notNull}")
    Integer question26;

    @NotNull(message = "{assessment.question27.notNull}")
    Integer question27;

    @NotNull(message = "{assessment.question28.notNull}")
    Integer question28;

    @NotNull(message = "{assessment.question29.notNull}")
    Integer question29;

    @NotNull(message = "{assessment.question30.notNull}")
    Integer question30;

    @NotNull(message = "{assessment.question31.notNull}")
    Integer question31;

    @NotNull(message = "{assessment.question32.notNull}")
    Integer question32;

    @NotNull(message = "{assessment.question33.notNull}")
    Integer question33;

    @NotNull(message = "{assessment.question34.notNull}")
    Integer question34;

    @NotNull(message = "{assessment.question35.notNull}")
    Integer question35;

    public Integer answerSum() {
        return question1 +
                question2 +
                question3 +
                question4 +
                question5 +
                question6 +
                question7 +
                question8 +
                question9 +
                question10 +
                question11 +
                question12 +
                question13 +
                question14 +
                question15 +
                question16 +
                question17 +
                question18 +
                question19 +
                question20 +
                question21 +
                question22 +
                question23 +
                question24 +
                question25 +
                question26 +
                question27 +
                question28 +
                question29 +
                question30 +
                question31 +
                question32 +
                question33 +
                question34 +
                question35;
    }

}
