
-- Adds a new column for the camera name to item table
ALTER TABLE item
  ADD COLUMN camera VARCHAR_IGNORECASE(255);
