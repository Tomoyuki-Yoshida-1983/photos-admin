package jp.yoshida.photos_admin.service;

import jp.yoshida.photos_admin.service.dto.PhotoDto;
import jp.yoshida.photos_admin.common.exception.PhotosBusinessException;
import jp.yoshida.photos_admin.common.exception.PhotosSystemException;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotosService {

    @NonNull
    List<PhotoDto> getPhotos();

    @NonNull
    PhotoDto getPhoto(@NonNull int id) throws PhotosBusinessException;

    void addPhoto(@NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException;

    void deletePhotos(@NonNull int[] ids) throws PhotosBusinessException;
}
