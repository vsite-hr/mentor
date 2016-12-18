DELETE FROM public.lecture_units WHERE lecture_id='200ceefa-a476-11e6-aedd-4485006d0fd8';

DELETE FROM public.units WHERE unit_id='cd519d4a-a476-11e6-aedd-4485006d0fd8'::uuid;

UPDATE public.units SET next_unit_id='6aeeab14-c566-11e6-b953-507b9dc82ab8'::uuid WHERE unit_id='8a6641d8-c498-11e6-b953-507b9dc82ab8'::uuid;

INSERT INTO units (unit_id, unit_type, unit_title, author_id, unit_attributes, unit_keywords, next_unit_id) VALUES ('4db0eef4-c566-11e6-b953-507b9dc82ab8', 'Image', 'A sinusoidal alternating voltage', '5da6bf98-a467-11e6-aedd-4485006d0fd8', '{"width": 400, "height": 400, "caption": "1 = peak, also amplitude,\n2 = peak-to-peak,\n3 = effective value,\n4 = Period", "filename": "sinusspannung.png", "contentType": "image/png"}', NULL, NULL);
INSERT INTO units (unit_id, unit_type, unit_title, author_id, unit_attributes, unit_keywords, next_unit_id) VALUES ('6aeeab14-c566-11e6-b953-507b9dc82ab8', 'Image', 'Tesla''s US390721 patent for a "Dynamo Electric Machine"', '5da6bf98-a467-11e6-aedd-4485006d0fd8', '{"width": 384, "height": 600, "filename": "US390721.png", "contentType": "image/png"}', NULL, 'a7e3eaee-c498-11e6-b953-507b9dc82ab8');

INSERT INTO lecture_units (lecture_id, unit_id, unit_ordinal) VALUES ('200ceefa-a476-11e6-aedd-4485006d0fd8', '8faae33a-b27b-11e6-aedd-4485006d0fd8', 1);
INSERT INTO lecture_units (lecture_id, unit_id, unit_ordinal) VALUES ('200ceefa-a476-11e6-aedd-4485006d0fd8', 'b8ebe73e-a476-11e6-aedd-4485006d0fd8', 2);
INSERT INTO lecture_units (lecture_id, unit_id, unit_ordinal) VALUES ('200ceefa-a476-11e6-aedd-4485006d0fd8', '4db0eef4-c566-11e6-b953-507b9dc82ab8', 3);
INSERT INTO lecture_units (lecture_id, unit_id, unit_ordinal) VALUES ('200ceefa-a476-11e6-aedd-4485006d0fd8', 'f74951d8-c497-11e6-b953-507b9dc82ab8', 4);
