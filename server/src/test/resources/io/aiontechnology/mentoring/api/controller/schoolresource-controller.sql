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

INSERT INTO school (id, name, street1, street2, city, state, zip, phone, district, is_private, is_active) VALUES ('fd03c21f-cd39-4c05-b3f1-6d49618b6b10', 'NAME', 'STREET1', 'STREET2', 'CITY', 'ST', '123456789', '3601234567', 'DISTRICT', true, true);
INSERT INTO school (id, name, street1, street2, city, state, zip, phone, district, is_private, is_active) VALUES ('b61cbdc4-b37e-429d-b33a-108f9753a073', 'NAME', 'STREET1', 'STREET2', 'CITY', 'ST', '123456789', '3601234567', 'DISTRICT', true, true);

INSERT INTO book (id, title, author, grade_level, is_active, location) VALUES ('f53af381-d524-40f7-8df9-3e808c9ad46b', 'TITLE1', 'AUTHOR1', 1, true, 'OFFLINE');
INSERT INTO book (id, title, author, grade_level, is_active, location) VALUES ('6e4da9bd-5387-45dc-9714-fb96387da770', 'TITLE2', 'AUTHOR2', 1, true, 'OFFLINE');

INSERT INTO school_book(school_id, book_id) VALUES ('b61cbdc4-b37e-429d-b33a-108f9753a073', 'f53af381-d524-40f7-8df9-3e808c9ad46b');
INSERT INTO school_book(school_id, book_id) VALUES ('b61cbdc4-b37e-429d-b33a-108f9753a073', '6e4da9bd-5387-45dc-9714-fb96387da770');

INSERT INTO game (id, name, grade1, grade2, is_active, location) VALUES ('81b4fd55-0f1d-45d9-9625-9bc3a367fe04', 'GAME1', 1, 2, true, 'OFFLINE');
INSERT INTO game (id, name, grade1, grade2, is_active, location) VALUES ('98866d5e-8dd8-433d-bdb5-dbd7fe8dce81', 'GAME2', 1, 2, true, 'OFFLINE');

INSERT INTO school_game(school_id, game_id) VALUES ('b61cbdc4-b37e-429d-b33a-108f9753a073', '81b4fd55-0f1d-45d9-9625-9bc3a367fe04');
INSERT INTO school_game(school_id, game_id) VALUES ('b61cbdc4-b37e-429d-b33a-108f9753a073', '98866d5e-8dd8-433d-bdb5-dbd7fe8dce81');
