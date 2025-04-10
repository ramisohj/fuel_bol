-- This allows to store time and zone only for Bolivia

ALTER TABLE fuel_tanks
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'America/La_Paz';