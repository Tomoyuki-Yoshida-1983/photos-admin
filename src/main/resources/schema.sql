CREATE TABLE IF NOT EXISTS photos (
    id INT NOT NULL AUTO_INCREMENT,
    raw_photo BLOB NOT NULL,
    thumbnail BLOB NOT NULL,
    file_name VARCHAR NOT NULL,
    mime_type VARCHAR NOT NULL,
    width VARCHAR NULL,
    height VARCHAR NULL,
    shooting_date_time TIMESTAMP NULL,
    latitude VARCHAR NULL,
    latitude_ref VARCHAR NULL,
    longitude VARCHAR NULL,
    longitude_ref VARCHAR NULL,
    PRIMARY KEY (id)
);
