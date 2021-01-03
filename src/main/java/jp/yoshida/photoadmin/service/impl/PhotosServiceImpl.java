package jp.yoshida.photoadmin.service.impl;

import com.drew.imaging.ImageProcessingException;
import jp.yoshida.photoadmin.common.util.PhotosUtil;
import jp.yoshida.photoadmin.dao.PhotosDao;
import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.service.PhotosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotosServiceImpl implements PhotosService {

    private final PhotosDao photosDao;

    @NonNull
    @Override
    public List<PhotoDto> getPhotos() {

        return photosDao.getPhotos();
    }

    @Nullable
    @Override
    public PhotoDto getPhoto(@NonNull int id) {

        List<Integer> ids = photosDao.getPhotoIds();
        PhotoDto photoDto = photosDao.getPhoto(id);

        int currentIdx = ids.indexOf(id);

        if (currentIdx >= 0 && ids.size() > currentIdx + 1) {
            photoDto.setNextId(ids.get(currentIdx + 1));
        }

        if (currentIdx > 0) {
            photoDto.setPrevId(ids.get(currentIdx - 1));
        }

        return photoDto;
    }

    @Transactional
    @Override
    public void addPhoto(@NonNull PhotoDto photoDto) throws IOException, ImageProcessingException {

        String fileName = photoDto.getSendingPhoto().getOriginalFilename();
        photoDto.setFileName(fileName);
        String extension = fileName.substring(fileName.lastIndexOf("."));
        switch (extension.toLowerCase()) {
            case ".jpg":
            case ".jpeg":
            case ".jpe":
            case ".jfif":
                photoDto.setExtension("jpeg");
                break;
            case ".png":
                photoDto.setExtension("png");
                break;
            default:
                break;
        }

        photoDto.setThumbnail(PhotosUtil.createThumbnail(photoDto.getSendingPhoto(), photoDto.getExtension()));
        photoDto.setRawPhoto(photoDto.getSendingPhoto().getBytes());
        PhotosUtil.setMetaDatum(photoDto);
        photosDao.addPhoto(photoDto);
    }
}
