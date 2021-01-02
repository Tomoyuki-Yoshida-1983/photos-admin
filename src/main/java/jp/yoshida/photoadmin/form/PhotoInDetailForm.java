package jp.yoshida.photoadmin.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoInDetailForm {

    private String rawPhoto;

    private MultipartFile thumbnail;

    private String fileName;

    private String extension;

    private String width;

    private String height;

    private String shootingDateTime;

    private String latitude;

    private String longitude;

}
