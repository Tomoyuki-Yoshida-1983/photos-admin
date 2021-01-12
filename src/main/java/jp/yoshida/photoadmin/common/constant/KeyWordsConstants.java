package jp.yoshida.photoadmin.common.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class KeyWordsConstants {

    public static final String KEY_MESSAGE = "message";

    public static final String KEY_INFO_LEVEL = "infoLevel";

    public static final String MIME_TYPE_ANY_IMAGE = "image/.*";

    public static final String IMAGE_FORMAT_NAME_JPG = "jpg";

    public static final String NAME_BINDING_RESULT_PHOTO_FORM
            = "org.springframework.validation.BindingResult.photoForm";

    public static final String NAME_PHOTO_FORM = "photoForm";

    public static final String NAME_BINDING_RESULT_DELETE_FORM
            = "org.springframework.validation.BindingResult.deleteForm";

    public static final String NAME_DELETE_FORM = "deleteForm";

    public static final String SYMBOL_LINE_FEED = "\n";

    public static final String SYMBOL_BLANK = "";

    public static final String SYMBOL_SPACE = " ";

    public static final int MAX_FILE_NAME_LENGTH = 50;

    public static final int MAX_FILE_SIZE_COEFFICIENT = 1;

    public static final int MAX_META_DATA_TEXT_LENGTH = 50;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum UNIT_FILE_SIZE {
        BYTE("B", 1L),
        KILO_BYTE("KB", 1024L),
        MEGA_BYTE("MB", 1024L * 1024L),
        GIGA_BYTE("GB", 1024L * 1024L * 1024L),
        ;

        @Getter
        private final String unit;

        @Getter
        private final long magnification;

        @Override
        public String toString() {
            return this.getUnit();
        }
    }
}
