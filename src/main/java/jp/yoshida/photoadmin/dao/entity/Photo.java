package jp.yoshida.photoadmin.dao.entity;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import lombok.Data;

import javax.validation.constraints.Max;
import java.util.Date;

@Data
public class Photo {

    private Integer id;

    private byte[] rawPhoto;

    private byte[] thumbnail;

    private String fileName;

    private String mimeType;

    @Max(KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String width;

    @Max(KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String height;

    private Date shootingDateTime;

    @Max(KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String latitude;

    @Max(KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String latitudeRef;

    @Max(KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String longitude;

    @Max(KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String longitudeRef;
}
