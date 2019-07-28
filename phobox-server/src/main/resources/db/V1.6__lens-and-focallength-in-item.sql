
-- Adds a new column for the lens and the focal length to item table

ALTER TABLE item
  ADD COLUMN lens VARCHAR_IGNORECASE(255);

ALTER TABLE item
  ADD COLUMN focal_length VARCHAR_IGNORECASE(255);
