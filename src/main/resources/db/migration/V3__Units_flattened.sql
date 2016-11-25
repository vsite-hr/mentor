GRANT ALL ON TABLE public.course_lectures TO mentor;
ALTER TABLE public.units ADD COLUMN unit_attributes jsonb;
INSERT INTO public.units(unit_id, unit_type, unit_title, author_id, unit_attributes) SELECT unit_id, unit_type, unit_title, author_id, json_build_object('markupType', unit_markup_type::text, 'markup', unit_markup) FROM units_text;
DROP TABLE public.units_text;
ALTER TABLE public.units ALTER COLUMN unit_attributes SET NOT NULL;

DROP TYPE public.markup_type;

UPDATE public.units SET unit_attributes = unit_attributes || jsonb_build_object('textUnitType', 'Mentor');

ALTER TABLE public.units ADD COLUMN unit_keywords text[];

