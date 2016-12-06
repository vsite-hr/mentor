ALTER TYPE public.unit_type ADD VALUE 'YouTube' AFTER 'Series';

UPDATE units SET unit_attributes = unit_attributes - 'textUnitType' WHERE unit_type = 'Text'::unit_type;
UPDATE units SET unit_attributes = unit_attributes - 'videoUnitType' WHERE unit_type = 'Video'::unit_type;
