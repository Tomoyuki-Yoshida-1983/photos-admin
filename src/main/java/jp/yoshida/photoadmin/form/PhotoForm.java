package jp.yoshida.photoadmin.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PhotoForm {

    private List<PhotoInDetailForm> photoInDetailForms;

    private MultipartFile sendingPhoto;

}
