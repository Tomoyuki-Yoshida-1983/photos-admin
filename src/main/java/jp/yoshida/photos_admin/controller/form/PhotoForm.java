package jp.yoshida.photos_admin.controller.form;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import jp.yoshida.photos_admin.controller.form.validation.ImageFile;
import jp.yoshida.photos_admin.controller.form.validation.MaxFileNameLength;
import jp.yoshida.photos_admin.controller.form.validation.MaxFileSize;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 写真フォーム
 */
@Data
public class PhotoForm {

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

    /**
     * アップロードされた写真ファイル
     */
    @NotNull
    @ImageFile
    @MaxFileNameLength(KeyWordsConstants.MAX_FILE_NAME_LENGTH)
    @MaxFileSize(
            value = KeyWordsConstants.MAX_FILE_SIZE_COEFFICIENT,
            unit = KeyWordsConstants.UNIT_FILE_SIZE.MEGA_BYTE)
    private MultipartFile sendingPhoto;
}
