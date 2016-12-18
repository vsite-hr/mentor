ALTER TABLE public.units ADD COLUMN next_unit_id uuid;
ALTER TABLE public.units ADD CONSTRAINT fk_units_next_unit_id FOREIGN KEY (next_unit_id) REFERENCES public.units (unit_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

