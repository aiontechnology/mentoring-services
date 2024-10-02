-- Copyright 2020-2021 Aion Technology LLC
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

INSERT INTO interest (id, name) VALUES ('548fe589-2765-4b5f-8552-fa40c0722216', 'INTEREST1');
INSERT INTO interest (id, name) VALUES ('c2a542fa-cf79-485c-bf88-25d3302ab170', 'INTEREST2');
INSERT INTO leadership_skill (id, name) VALUES ('81aea425-4895-462d-9b9f-48a3150b7e49', 'LEADERSHIP_SKILL1');
INSERT INTO leadership_skill (id, name) VALUES ('d14ae031-b14e-4c9a-a9ba-d67703e80c29', 'LEADERSHIP_SKILL2');
INSERT INTO leadership_trait (id, name) VALUES ('ca764852-4973-4c24-8a46-4a61ad000574', 'LEADERSHIP_TRAIT1');
INSERT INTO leadership_trait (id, name) VALUES ('6b83bbae-9123-49b4-aedd-852949a50ae5', 'LEADERSHIP_TRAIT2');
INSERT INTO phonogram (id, name) VALUES ('8299b496-43f4-436d-9d29-c862fd148232', 'PH1');
INSERT INTO phonogram (id, name) VALUES ('7b3fed7c-2957-454f-ab77-1aadfbde60ff', 'PH2');
INSERT INTO behavior (id, name) VALUES ('b0fd98b1-b7f1-4752-8a38-d477e201d8c1', 'BEHAVIOR1');
INSERT INTO behavior (id, name) VALUES ('091099ba-a724-46fb-b2a9-b4bc511a1049', 'BEHAVIOR2');
INSERT INTO tag (id, name) VALUES ('16eb1389-7842-4753-bd70-0e99f0af9913', 'TAG');

INSERT INTO book (id, title, author, grade_level, is_active, location) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', 'TITLE', 'AUTHOR', 1, true, 'OFFLINE');
INSERT INTO book_interest (book_id, interest_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', '548fe589-2765-4b5f-8552-fa40c0722216');
INSERT INTO book_leadershipskill (book_id, leadershipskill_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', '81aea425-4895-462d-9b9f-48a3150b7e49');
INSERT INTO book_leadershiptrait (book_id, leadershiptrait_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', 'ca764852-4973-4c24-8a46-4a61ad000574');
INSERT INTO book_phonogram (book_id, phonogram_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', '8299b496-43f4-436d-9d29-c862fd148232');
INSERT INTO book_phonogram (book_id, phonogram_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', '7b3fed7c-2957-454f-ab77-1aadfbde60ff');
INSERT INTO book_behavior (book_id, behavior_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', 'b0fd98b1-b7f1-4752-8a38-d477e201d8c1');
INSERT INTO book_behavior (book_id, behavior_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', '091099ba-a724-46fb-b2a9-b4bc511a1049');
INSERT INTO book_tag (book_id, tag_id) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', '16eb1389-7842-4753-bd70-0e99f0af9913');
