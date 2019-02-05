
ALTER TABLE item_albums
  DROP CONSTRAINT fk_albums_id;

ALTER TABLE item_albums
  ADD CONSTRAINT fk_albums_id FOREIGN KEY(albums_id) REFERENCES album(id) ON DELETE CASCADE;
