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

INSERT INTO school (id, name, is_active) VALUES ('fd03c21f-cd39-4c05-b3f1-6d49618b6b10', 'test', true);

INSERT INTO person (id, last_name, first_name, work_phone, cell_phone, email) VALUES ('2f10e8ac-9ad6-4771-a034-ca1d6c387b9b', 'Rogers', 'Fred', '3601112222', '3603334444', 'fred@rogers.com');
INSERT INTO person (id, last_name, first_name, work_phone, cell_phone, email) VALUES ('9dc31bec-8120-40c7-bcd2-6a2c4c85c033', 'Mentor', 'Mark', '3602223333', '3604445555', 'mark@mentor.com');

INSERT INTO school_person_role (id, school_id, person_id, type, grade1, grade2, is_active, location, is_media_release_signed) VALUES ('ba238442-ce51-450d-a474-2e36872abe05', 'fd03c21f-cd39-4c05-b3f1-6d49618b6b10', '2f10e8ac-9ad6-4771-a034-ca1d6c387b9b', 'TEACHER', 1, 2, true, 'OFFLINE', true);
INSERT INTO school_person_role (id, school_id, person_id, type, is_active, location, is_media_release_signed) VALUES ('46771afb-a8ef-474e-b8e5-c693529cc5a8', 'fd03c21f-cd39-4c05-b3f1-6d49618b6b10', '9dc31bec-8120-40c7-bcd2-6a2c4c85c033', 'MENTOR', true, 'OFFLINE', true);

INSERT INTO student (id, student_id, first_name, last_name, grade, preferred_time, location, is_media_release_signed, pre_behavioral_assessment, post_behavioral_assessment, is_active, school_id) VALUES ('2a8c5871-a21d-47a1-a516-a6376a6b8bf2', '1234', 'Sam', 'Student', 2, '2:00pm', 'OFFLINE', true, 1, 5, true, 'fd03c21f-cd39-4c05-b3f1-6d49618b6b10');
INSERT INTO student (id, student_id, first_name, last_name, grade, preferred_time, location, is_media_release_signed, pre_behavioral_assessment, post_behavioral_assessment, is_active, school_id) VALUES ('f0c08c26-954b-4d05-8536-522403f9e54e', '1234', 'Sam', 'Student', 2, '2:00pm', 'OFFLINE', true, 1, 5, true, 'fd03c21f-cd39-4c05-b3f1-6d49618b6b10');

INSERT INTO student_teacher (student_id, role_id, comment) values ('2a8c5871-a21d-47a1-a516-a6376a6b8bf2', 'ba238442-ce51-450d-a474-2e36872abe05', 'comment');
INSERT INTO student_teacher (student_id, role_id, comment) values ('f0c08c26-954b-4d05-8536-522403f9e54e', 'ba238442-ce51-450d-a474-2e36872abe05', 'comment');

INSERT INTO student_mentor (student_id, role_id, time) values ('2a8c5871-a21d-47a1-a516-a6376a6b8bf2', '46771afb-a8ef-474e-b8e5-c693529cc5a8', 'Monday morning');