-- Storage item
CREATE TABLE IF NOT EXISTS `item` (
	`path` varchar(500) NOT NULL,
	`rotation` int(10) DEFAULT 0,
	`width` int(10),
	`height` int(10),
	`creation` datetime,
	`found` datetime DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY( `path` )
);

-- Storage item tagging
CREATE TABLE IF NOT EXISTS `item_tag` (
	`id` int(10) NOT NULL auto_increment,
	`id_item` varchar(500) NOT NULL,
	`tag_value` varchar(255) NOT NULL,
	FOREIGN KEY(id_item) REFERENCES item(path),
	PRIMARY KEY( `id` )
);

-- Config
CREATE TABLE IF NOT EXISTS `config` (
	`key` varchar(500) NOT NULL,
	`value` varchar(1000),
	PRIMARY KEY( `key` )
);

-- Album
CREATE TABLE IF NOT EXISTS `album` (
	`id` int(10) NOT NULL auto_increment,
	`name` varchar(255),
	`creation` datetime DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY( `id` )
);

-- Album Item
CREATE TABLE IF NOT EXISTS `album_item` (
	`id` int(10) NOT NULL auto_increment,
	`id_album` int(10) NOT NULL,
	`path` varchar(500) NOT NULL,
	`order` int(10),
	FOREIGN KEY(id_album) REFERENCES album(id),
	FOREIGN KEY(path) REFERENCES item(path),
	PRIMARY KEY( `id` )
	
);