/**
 * 各カラムの精度は画面仕様上の入力項目の上限値よりも大きめに設定している。
 * 理由としては本番稼働後に入力項目の上限値の仕様が変更された場合にDBへの改修が及びにくくするため。
 */
CREATE TABLE IF NOT EXISTS photos (
    id INT NOT NULL AUTO_INCREMENT,
    raw_photo BLOB(1G) NOT NULL,
    thumbnail BLOB(1G) NOT NULL,
    file_name VARCHAR(10K) NOT NULL,
    mime_type VARCHAR(10K) NOT NULL,
    width VARCHAR(10K) NULL,
    height VARCHAR(10K) NULL,
    shooting_date_time TIMESTAMP NULL,
    latitude VARCHAR(10K) NULL,
    latitude_ref VARCHAR(10K) NULL,
    longitude VARCHAR(10K) NULL,
    longitude_ref VARCHAR(10K) NULL,
    PRIMARY KEY (id)
);
