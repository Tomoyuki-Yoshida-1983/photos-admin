package jp.yoshida.photoadmin.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class PhotoDto {

    private Integer id;

    private byte[] rawPhoto;

    private byte[] thumbnail;

    private String fileName;

    private String mimeType;

    private String width;

    private String height;

    private Date shootingDateTime;

    private String latitude;

    private String latitudeRef;

    private String longitude;

    private String longitudeRef;
}
