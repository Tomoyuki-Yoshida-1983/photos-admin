package jp.yoshida.photos_admin.common.constant;

/**
 * メッセージ定数
 */
public class MessagesConstants {

    public static final String ERROR_FILE_IS_NOT_IMAGE
            = "{jp.yoshida.photos_admin.controller.form.validation.ImageFile.message}";

    public static final String ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS
            = "{jp.yoshida.photos_admin.controller.form.validation.PossibleFileName.message}";

    public static final String ERROR_FILE_NAME_IS_EMPTY
            = "{jp.yoshida.photos_admin.controller.form.validation.MaxFileNameLength.empty.message}";

    public static final String ERROR_FILE_NAME_LENGTH_EXCEEDED
            = "{jp.yoshida.photos_admin.controller.form.validation.MaxFileNameLength.message}";

    public static final String ERROR_FILE_SIZE_EXCEEDED
            = "{jp.yoshida.photos_admin.controller.form.validation.MaxFileSize.message}";

    public static final String INFO_LEVEL_INFO = "info";

    public static final String INFO_LEVEL_WARN = "warn";

    public static final String INFO_LEVEL_ERROR = "error";

    public static final String INFO_ADD_SUCCESS = "INFO_ADD_SUCCESS";

    public static final String INFO_DELETE_SUCCESS = "INFO_DELETE_SUCCESS";

    public static final String WARN_PHOTO_NOT_FOUND = "WARN_PHOTO_NOT_FOUND";

    public static final String ERROR_TEMP_FILE_PROCESSING_FAILED = "ERROR_TEMP_FILE_PROCESSING_FAILED";

    public static final String ERROR_FILE_PROCESSING_FAILED = "ERROR_FILE_PROCESSING_FAILED";
}
