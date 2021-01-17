package jp.yoshida.photosadmin.service;

import jp.yoshida.photosadmin.service.dto.PhotoDto;
import jp.yoshida.photosadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photosadmin.common.exception.PhotosSystemException;
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
