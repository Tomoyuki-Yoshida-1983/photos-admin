package jp.yoshida.photoadmin.service.impl;

import jp.yoshida.photoadmin.common.constant.MessagesConstants;
import jp.yoshida.photoadmin.common.constant.StandardsConstants;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.exception.PhotosSystemException;
import jp.yoshida.photoadmin.common.util.PhotosUtil;
import jp.yoshida.photoadmin.dao.PhotosDao;
import jp.yoshida.photoadmin.dao.entity.Photo;
import jp.yoshida.photoadmin.service.PhotosService;
import jp.yoshida.photoadmin.service.dto.PhotoDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PhotosServiceImpl implements PhotosService {

    private final PhotosDao photosDao;

    private final Validator validator;

    @NonNull
    @Override
    public List<PhotoDto> getPhotos() {

        List<PhotoDto> photoEntities = new ArrayList<>();

        for (Photo photo : photosDao.getPhotos()) {
            PhotoDto photoDto = new PhotoDto();
            BeanUtils.copyProperties(photo, photoDto);
            photoDto.setThumbnail(Base64.getEncoder().encodeToString(photo.getThumbnail()));

            if (Objects.nonNull(photo.getShootingDateTime())) {
                photoDto.setShootingDateTime(
                        StandardsConstants.SIMPLE_DATE_FORMAT.format(photo.getShootingDateTime()));
            }

            photoEntities.add(photoDto);
        }

        return photoEntities;
    }

    @NonNull
    @Override
    public PhotoDto getPhoto(@NonNull int id) throws PhotosBusinessException {

        List<Integer> ids = photosDao.getPhotoIds();
        Photo photo = photosDao.getPhoto(id);
        int currentIdx = ids.indexOf(id);

        if (currentIdx == -1 || Objects.isNull(photo)) {
            throw new PhotosBusinessException(
                    MessagesConstants.WARN_PHOTO_NOT_FOUND,
                    MessagesConstants.INFO_LEVEL_WARN);
        }

        PhotoDto photoDto = new PhotoDto();
        BeanUtils.copyProperties(photo, photoDto);
        photoDto.setRawPhoto(Base64.getEncoder().encodeToString(photo.getRawPhoto()));

        if (Objects.nonNull(photo.getShootingDateTime())) {
            photoDto.setShootingDateTime(
                    StandardsConstants.SIMPLE_DATE_FORMAT.format(photo.getShootingDateTime()));
        }

        if (ids.size() > currentIdx + 1) {
            photoDto.setNextId(ids.get(currentIdx + 1));
        }

        if (currentIdx > 0) {
            photoDto.setPrevId(ids.get(currentIdx - 1));
        }

        return photoDto;
    }

    @Transactional
    @Override
    public void addPhoto(
            @NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException {

        Photo photo = new Photo();
        photo.setFileName(sendingPhoto.getOriginalFilename());
        photo.setMimeType(sendingPhoto.getContentType());

        try {
            photo.setRawPhoto(sendingPhoto.getBytes());
        } catch (Exception e) {
            throw new PhotosBusinessException(
                    MessagesConstants.ERROR_FILE_PROCESSING_FAILED,
                    MessagesConstants.INFO_LEVEL_ERROR);
        }

        photo.setThumbnail(PhotosUtil.createThumbnail(sendingPhoto));
        PhotosUtil.setMetaDatum(photo, sendingPhoto);
        Set<ConstraintViolation<Photo>> violations = validator.validate(photo);

        if (violations.size() > 0) {
            throw new PhotosBusinessException(
                    MessagesConstants.ERROR_FILE_PROCESSING_FAILED,
                    MessagesConstants.INFO_LEVEL_ERROR);
        }

        photosDao.addPhoto(photo);
    }

    @Override
    public void deletePhotos(@NonNull int[] ids) {

        photosDao.deletePhotos(ids);
    }
}
