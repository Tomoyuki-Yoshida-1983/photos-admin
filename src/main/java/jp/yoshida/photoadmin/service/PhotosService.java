package jp.yoshida.photoadmin.service;

import com.drew.imaging.ImageProcessingException;
import jp.yoshida.photoadmin.PhotoEntity;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotosService {

    @NonNull
    List<PhotoEntity> getPhotos();

    @NonNull
    PhotoEntity getPhoto(@NonNull int id);

    void addPhoto(@NonNull MultipartFile sendingPhoto) throws IOException, ImageProcessingException;

    void deletePhotos(@NonNull int[] ids);
}
