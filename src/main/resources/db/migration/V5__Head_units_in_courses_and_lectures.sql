ALTER TABLE public.courses ADD COLUMN course_head_unit_id uuid;
ALTER TABLE public.courses ADD CONSTRAINT fk_courses_head_unit_id FOREIGN KEY (course_head_unit_id) REFERENCES public.units (unit_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.lectures ADD COLUMN lecture_head_unit_id uuid;
ALTER TABLE public.lectures ADD CONSTRAINT fk_lectures_head_unit_id FOREIGN KEY (lecture_head_unit_id) REFERENCES public.units (unit_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

