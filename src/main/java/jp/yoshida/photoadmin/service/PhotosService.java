package jp.yoshida.photoadmin.service;

import jp.yoshida.photoadmin.service.dto.PhotoDto;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.exception.PhotosSystemException;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotosService {

    @NonNull
    List<PhotoDto> getPhotos();

    @NonNull
    PhotoDto getPhoto(@NonNull int id) throws PhotosBusinessException;

    void addPhoto(@NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException;

    void deletePhotos(@NonNull int[] ids);
}
