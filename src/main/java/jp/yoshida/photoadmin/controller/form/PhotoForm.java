package jp.yoshida.photoadmin.controller.form;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photoadmin.common.constant.MessagesConstants;
import jp.yoshida.photoadmin.controller.form.validation.ImageFile;
import jp.yoshida.photoadmin.controller.form.validation.MaxFileNameLength;
import jp.yoshida.photoadmin.controller.form.validation.MaxFileSizeGb;
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

    @NotNull(message = MessagesConstants.ERROR_NO_FILE)
    @ImageFile
    @MaxFileNameLength(KeyWordsConstants.MAX_FILE_NAME_LENGTH)
    @MaxFileSizeGb(KeyWordsConstants.MAX_FILE_SIZE_GB)
    private MultipartFile sendingPhoto;
}
