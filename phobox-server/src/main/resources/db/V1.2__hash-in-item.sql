
-- Adds a new column for the hash to item table
ALTER TABLE item
  ADD COLUMN hash VARCHAR_IGNORECASE(255);
