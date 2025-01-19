/*
 * Copyright 2021-2023 Aion Technology LLC
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

package io.aiontechnology.mentoring.service;

/**
 * @author Whitney Hunter
 * @since 0.10.0
 */
public class InterestServiceTest {

//    @Test
//    void testCreateNewInterest_doesNotExist() throws Exception {
//        // setup the fixture
//        String interestValue = "VALUE";
//
//        Map<String, Interest> interests = new HashMap<>();
//
//        InterestService interestService = new InterestService(mock(InterestRepository.class));
//
//        // execute the SUT
//        Optional<Interest> result = interestService.createNewInterest(interests, interestValue);
//
//        // validation
//        assertThat(result).isNotEmpty();
//        assertThat(result.get().getName()).isEqualTo(interestValue);
//    }
//
//    @Test
//    void testCreateNewInterest_exists() throws Exception {
//        // setup the fixture
//        String interestValue = "VALUE";
//        Interest interest = new Interest();
//        interest.setName(interestValue);
//
//        Map<String, Interest> interests = new HashMap<>();
//        interests.put(interestValue, interest);
//
//        InterestService interestService = new InterestService(mock(InterestRepository.class));
//
//        // execute the SUT
//        Optional<Interest> result = interestService.createNewInterest(interests, interestValue);
//
//        // validation
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void testUpdateExistingInterest_exists() throws Exception {
//        // setup the fixture
//        String interestValue = "VALUE";
//        UUID interestUUID = UUID.randomUUID();
//        Interest interest = new Interest();
//        interest.setId(interestUUID);
//        interest.setName(interestValue);
//
//        Map<String, Interest> interests = new HashMap<>();
//        interests.put(interestValue, interest);
//
//        InterestService interestService = new InterestService(mock(InterestRepository.class));
//
//        String newInterestValue = "NEW VALUE";
//
//        // execute the SUT
//        Optional<Interest> result = interestService.updateExistingInterest(interests, interestValue,
//        newInterestValue);
//
//        // validation
//        assertThat(result).isNotEmpty();
//        assertThat(result.get().getId()).isEqualTo(interestUUID);
//        assertThat(result.get().getName()).isEqualTo(newInterestValue);
//    }
//
//    @Test
//    void testUpdateExistingInterest_doesNotExist() throws Exception {
//        // setup the fixture
//        String interestValue = "VALUE";
//
//        Map<String, Interest> interests = new HashMap<>();
//
//        InterestService interestService = new InterestService(mock(InterestRepository.class));
//
//        String newInterestValue = "NEW VALUE";
//
//        // execute the SUT
//        Optional<Interest> result = interestService.updateExistingInterest(interests, interestValue,
//        newInterestValue);
//
//        // validation
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void testUpdateInterests_Old() throws Exception {
//        // setup the fixture
//        String interestValue1 = "VALUE";
//        UUID interestUUID1 = UUID.randomUUID();
//        Interest interest = new Interest();
//        interest.setId(interestUUID1);
//        interest.setName(interestValue1);
//
//        InterestRepository interestRepository = mock(InterestRepository.class);
//        when(interestRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(interest));
//        when(interestRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
//
//        InterestService interestService = new InterestService(interestRepository);
//
//        String newValue = "NEW_VALUE";
//
//        Map<String, String> values = new HashMap<>();
//        values.put(newValue, interestValue1);
//
//        // execute the SUT
//        interestService.updateInterests(values);
//
//        // validation
//        verify(interestRepository).save(any());
//    }
//
//    @Test
//    void testUpdateInterests_New() throws Exception {
//        // setup the fixture
//        InterestRepository interestRepository = mock(InterestRepository.class);
//        when(interestRepository.findAllByOrderByNameAsc()).thenReturn(new ArrayList<>());
//        when(interestRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
//
//        InterestService interestService = new InterestService(interestRepository);
//
//        String newValue = "NEW_VALUE";
//
//        Map<String, String> values = new HashMap<>();
//        values.put(newValue, "CREATE_ME");
//
//        // execute the SUT
//        interestService.updateInterests(values);
//
//        // validation
//        verify(interestRepository).save(any());
//    }

}
