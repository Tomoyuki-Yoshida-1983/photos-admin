package jp.yoshida.photoadmin.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoForm {

    private Integer id;

    private Integer nextId;

    private Integer prevId;

    private String rawPhoto;

    private String thumbnail;

    private String fileName;

    private String extension;

    private String width;

    private String height;

    private String shootingDateTime;

    private String latitude;

    private String longitude;

    private MultipartFile sendingPhoto;
}
