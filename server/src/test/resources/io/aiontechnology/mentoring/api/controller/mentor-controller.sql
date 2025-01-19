-- Copyright 2020 Aion Technology LLC
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

INSERT INTO school_person_role (id, school_id, person_id, type, availability, is_active) VALUES ('ba238442-ce51-450d-a474-2e36872abe05', 'fd03c21f-cd39-4c05-b3f1-6d49618b6b10', '2f10e8ac-9ad6-4771-a034-ca1d6c387b9b', 'MENTOR', 'anytime', true);
