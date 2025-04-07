
-- added for batch operations
CREATE SEQUENCE fuel_tank_seq START WITH 2249055 INCREMENT BY 100;

-----------------------------------------------------------------------------------
-- IN ORDER TO UPDATE THE SEQ: MAKE THE NEXT STEPS:
-- 1. Get the current max ID in the fuel_tanks table
SELECT MAX(id_fuel_tank) FROM fuel_tanks;

-- 2. Alter the sequence to increment by 100 (or match your Hibernate allocationSize)
ALTER SEQUENCE fuel_tank_seq INCREMENT BY 100;

-- 3. Restart the sequence to avoid ID conflicts (set it to max + 1)
ALTER SEQUENCE fuel_tank_seq RESTART WITH 2251661;