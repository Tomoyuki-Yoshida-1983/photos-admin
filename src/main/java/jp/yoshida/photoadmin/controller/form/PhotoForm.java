package jp.yoshida.photoadmin.controller.form;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photoadmin.controller.form.validation.ImageFile;
import jp.yoshida.photoadmin.controller.form.validation.MaxFileNameLength;
import jp.yoshida.photoadmin.controller.form.validation.MaxFileSize;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class PhotoForm {

    private Integer id;

    private Integer nextId;

    private Integer prevId;

    private String rawPhoto;

    private String thumbnail;

    private String fileName;

    private String mimeType;

    private String width;

    private String height;

    private String shootingDateTime;

    private String latitude;

    private String longitude;

    @NotNull
    @ImageFile
    @MaxFileNameLength(KeyWordsConstants.MAX_FILE_NAME_LENGTH)
    @MaxFileSize(
            value = KeyWordsConstants.MAX_FILE_SIZE_COEFFICIENT,
            unit = KeyWordsConstants.UNIT_FILE_SIZE.MEGA_BYTE)
    private MultipartFile sendingPhoto;
}
