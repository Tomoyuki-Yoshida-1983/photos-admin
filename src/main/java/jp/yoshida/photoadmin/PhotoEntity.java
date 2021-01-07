package jp.yoshida.photoadmin;

import lombok.Data;

@Data
public class PhotoEntity {

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
}
