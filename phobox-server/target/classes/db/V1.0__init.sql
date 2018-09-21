-- Album
CREATE TABLE album (
    `id` INTEGER NOT NULL auto_increment,
    `creation` DATE NOT NULL,
    `name` VARCHAR_IGNORECASE(255) NOT NULL,
    PRIMARY KEY( `id` )
);

-- Config
CREATE TABLE config (
    `key` VARCHAR_IGNORECASE(255) NOT NULL,
    `value` VARCHAR_IGNORECASE(255) NOT NULL,
    PRIMARY KEY( `key` )
);

-- Item
CREATE TABLE item(
    `uid` VARCHAR_IGNORECASE(255) NOT NULL,
    `full_path` VARCHAR_IGNORECASE(255) NOT NULL,
    `path` VARCHAR_IGNORECASE(255) NOT NULL,
    `file_name` VARCHAR_IGNORECASE(255) NOT NULL,
    `file_extension` VARCHAR_IGNORECASE(255) NOT NULL,
    `description` VARCHAR_IGNORECASE(255),
    `width` INTEGER,
    `height` INTEGER,
    `rotation` INTEGER,
    `creation` DATE,
    `imported` DATE,
    PRIMARY KEY( `uid` )
);

-- TAG 
CREATE TABLE item_tag (
    `name` VARCHAR_IGNORECASE(255) NOT NULL,
    `type` VARCHAR_IGNORECASE(255),
    PRIMARY KEY( `name` )
);

-- Item album relation
CREATE TABLE item_albums (
    `items_uid` VARCHAR_IGNORECASE(255) NOT NULL,
    `albums_id` INTEGER NOT NULL,
    FOREIGN KEY(albums_id) REFERENCES album(id) NOCHECK,
    FOREIGN KEY(items_uid) REFERENCES item(`uid`) NOCHECK
);

-- Item tag relation
CREATE TABLE item_tags (
    `items_uid` VARCHAR_IGNORECASE(255) NOT NULL,
    `tags_name` VARCHAR_IGNORECASE(255) NOT NULL,
    FOREIGN KEY(tags_name) REFERENCES item_tag(name) NOCHECK,
    FOREIGN KEY(items_uid) REFERENCES item(`uid`) NOCHECK
);









