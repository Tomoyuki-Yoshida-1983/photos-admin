package jp.yoshida.photoadmin.dao.entity;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class Photo {

    private Integer id;

    private byte[] rawPhoto;

    private byte[] thumbnail;

    private String fileName;

    private String mimeType;

    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String width;

    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String height;

    private Date shootingDateTime;

    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String latitude;

    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String latitudeRef;

    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String longitude;

    @Length(max = KeyWordsConstants.MAX_META_DATA_TEXT_LENGTH)
    private String longitudeRef;
}
