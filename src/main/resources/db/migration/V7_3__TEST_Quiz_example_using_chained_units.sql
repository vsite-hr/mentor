INSERT INTO units (unit_id, unit_type, unit_title, author_id, unit_attributes, unit_keywords, next_unit_id) VALUES ('a6d1353a-c56f-11e6-b953-507b9dc82ab8', 'Text', 'Answer', '5da6bf98-a467-11e6-aedd-4485006d0fd8', '{"markup": "AC can be transformed from a low voltage to a high voltage and back again with a tranformer. DC cannot.", "markupType": "None"}', NULL, NULL);
INSERT INTO units (unit_id, unit_type, unit_title, author_id, unit_attributes, unit_keywords, next_unit_id) VALUES ('9d70ba4c-c56f-11e6-b953-507b9dc82ab8', 'Text', 'Quiz', '5da6bf98-a467-11e6-aedd-4485006d0fd8', '{"markup": "AC is more easily distributed than DC. Why?", "markupType": "None"}', NULL, 'a6d1353a-c56f-11e6-b953-507b9dc82ab8');

INSERT INTO lecture_units (lecture_id, unit_id, unit_ordinal) VALUES ('200ceefa-a476-11e6-aedd-4485006d0fd8', '9d70ba4c-c56f-11e6-b953-507b9dc82ab8', 5);

