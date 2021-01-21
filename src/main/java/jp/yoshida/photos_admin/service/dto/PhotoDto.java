package jp.yoshida.photos_admin.service.dto;

import lombok.Data;

/**
 * 写真DTO
 */
@Data
public class PhotoDto {

    /**
     * 写真テーブルのID
     */
    private Integer id;

    /**
     * 次の写真のID
     */
    private Integer nextId;

    /**
     * 前の写真のID
     */
    private Integer prevId;

    /**
     * 写真（Base64でエンコードされた文字列として保持する）
     */
    private String rawPhoto;

    /**
     * サムネイル（Base64でエンコードされた文字列として保持する）
     */
    private String thumbnail;

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
    private String width;

    /**
     * 高さ
     */
    private String height;

    /**
     * 撮影日時
     */
    private String shootingDateTime;

    /**
     * 撮影場所の緯度
     */
    private String latitude;

    /**
     * 撮影場所の経度
     */
    private String longitude;
}
