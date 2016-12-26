ALTER TABLE public.lecture_units
  ADD CONSTRAINT fk_lecture_units_unit_id FOREIGN KEY (unit_id)
      REFERENCES public.units (unit_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

