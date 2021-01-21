package jp.yoshida.photos_admin.repository.entity;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 写真
 */
@Data
public class Photo {

    /**
     * 写真テーブルのID
     */
    private Integer id;

    /**
     * 写真
     */
    private byte[] rawPhoto;

    /**
     * サムネイル
     */
    private byte[] thumbnail;

    /**
     * ファイル名
     */
    private String fileName;

    /**
     * MIMEタイプ
     */
    private String mimeType;

    /**
     * 幅
     */
    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String width;

    /**
     * 高さ
     */
    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String height;

    /**
     * 撮影日時
     */
    private Date shootingDateTime;

    /**
     * 撮影場所の緯度
     */
    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String latitude;

    /**
     * 北緯／南緯区分
     */
    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String latitudeRef;

    /**
     * 撮影場所の経度
     */
    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String longitude;

    /**
     * 東経／西経区分
     */
    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String longitudeRef;
}
