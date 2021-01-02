CREATE TABLE IF NOT EXISTS photos (
    id INT NOT NULL AUTO_INCREMENT,
    raw_photo BLOB NOT NULL,
    thumbnail BLOB NOT NULL,
    file_name VARCHAR NOT NULL,
    extension VARCHAR NOT NULL,
    width VARCHAR NULL,
    height VARCHAR NULL,
    shooting_date_time VARCHAR NULL,
    latitude VARCHAR NULL,
    longitude VARCHAR NULL,
    PRIMARY KEY (id)
);
