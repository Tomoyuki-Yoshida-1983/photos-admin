package jp.yoshida.photoadmin.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoDto {

    private byte[] rawPhoto;

    private MultipartFile thumbnail;

    private String fileName;

    private String extension;

    private String width;

    private String height;

    private String shootingDateTime;

    private String latitude;

    private String longitude;

    private MultipartFile sendingPhoto;

}
