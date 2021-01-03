package jp.yoshida.photoadmin.form;

import lombok.Data;

import java.util.Date;

@Data
public class PhotoInDetailForm {

    private Integer id;

    private Integer nextId;

    private Integer prevId;

    private String rawPhoto;

    private String thumbnail;

    private String fileName;

    private String extension;

    private String width;

    private String height;

    private Date shootingDateTime;

    private String latitude;

    private String longitude;

}
