package jp.yoshida.photoadmin.service.impl;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photoadmin.common.constant.MessagesConstants;
import jp.yoshida.photoadmin.common.constant.StandardsConstants;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.exception.PhotosSystemException;
import jp.yoshida.photoadmin.common.util.PhotosUtil;
import jp.yoshida.photoadmin.repository.PhotosRepository;
import jp.yoshida.photoadmin.repository.entity.Photo;
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

    private final PhotosRepository photosRepository;

    private final Validator validator;

    @NonNull
    @Override
    public List<PhotoDto> getPhotos() {

        List<PhotoDto> photoDtos = new ArrayList<>();

        for (Photo photo : photosRepository.getPhotos()) {
            PhotoDto photoDto = new PhotoDto();
            BeanUtils.copyProperties(photo, photoDto);
            photoDto.setThumbnail(Base64.getEncoder().encodeToString(photo.getThumbnail()));
            photoDto.setLatitude(PhotosUtil.concatIgnoreNull(
                    photo.getLatitude(), photo.getLatitudeRef(), KeyWordsConstants.SYMBOL_SPACE));
            photoDto.setLongitude(PhotosUtil.concatIgnoreNull(
                    photo.getLongitude(), photo.getLongitudeRef(), KeyWordsConstants.SYMBOL_SPACE));

            if (Objects.nonNull(photo.getShootingDateTime())) {
                photoDto.setShootingDateTime(
                        StandardsConstants.SIMPLE_DATE_FORMAT.format(photo.getShootingDateTime()));
            }

            photoDtos.add(photoDto);
        }

        return photoDtos;
    }

    @NonNull
    @Override
    public PhotoDto getPhoto(@NonNull int id) throws PhotosBusinessException {

        List<Integer> ids = photosRepository.getPhotoIds();
        Photo photo = photosRepository.getPhoto(id);
        int currentIdx = ids.indexOf(id);

        if (currentIdx == -1 || Objects.isNull(photo)) {
            throw new PhotosBusinessException(
                    MessagesConstants.WARN_PHOTO_NOT_FOUND,
                    MessagesConstants.INFO_LEVEL_WARN);
        }

        PhotoDto photoDto = new PhotoDto();
        BeanUtils.copyProperties(photo, photoDto);
        photoDto.setRawPhoto(Base64.getEncoder().encodeToString(photo.getRawPhoto()));
        photoDto.setLatitude(PhotosUtil.concatIgnoreNull(
                photo.getLatitude(), photo.getLatitudeRef(), KeyWordsConstants.SYMBOL_SPACE));
        photoDto.setLongitude(PhotosUtil.concatIgnoreNull(
                photo.getLongitude(), photo.getLongitudeRef(), KeyWordsConstants.SYMBOL_SPACE));

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

        photosRepository.addPhoto(photo);
    }

    @Override
    public void deletePhotos(@NonNull int[] ids) throws PhotosBusinessException {

        if (photosRepository.deletePhotos(ids) == 0) {
            throw new PhotosBusinessException(
                    MessagesConstants.WARN_PHOTO_NOT_FOUND,
                    MessagesConstants.INFO_LEVEL_WARN);
        }
    }
}
