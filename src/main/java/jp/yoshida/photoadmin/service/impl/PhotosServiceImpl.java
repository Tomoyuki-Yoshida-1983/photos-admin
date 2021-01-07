package jp.yoshida.photoadmin.service.impl;

import jp.yoshida.photoadmin.PhotoEntity;
import jp.yoshida.photoadmin.common.constant.MessageConstants;
import jp.yoshida.photoadmin.common.constant.StandardConstants;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.exception.PhotosSystemException;
import jp.yoshida.photoadmin.common.util.PhotosUtil;
import jp.yoshida.photoadmin.dao.PhotosDao;
import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.service.PhotosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PhotosServiceImpl implements PhotosService {

    private final PhotosDao photosDao;

    @NonNull
    @Override
    public List<PhotoEntity> getPhotos() {

        List<PhotoEntity> photoEntities = new ArrayList<>();

        for (PhotoDto photoDto : photosDao.getPhotos()) {
            PhotoEntity photoEntity = new PhotoEntity();
            BeanUtils.copyProperties(photoDto, photoEntity);
            photoEntity.setThumbnail(Base64.getEncoder().encodeToString(photoDto.getThumbnail()));

            if (Objects.nonNull(photoDto.getShootingDateTime())) {
                photoEntity.setShootingDateTime(
                        StandardConstants.SIMPLE_DATE_FORMAT.format(photoDto.getShootingDateTime()));
            }

            photoEntities.add(photoEntity);
        }

        return photoEntities;
    }

    @NonNull
    @Override
    public PhotoEntity getPhoto(@NonNull int id) throws PhotosBusinessException {

        List<Integer> ids = photosDao.getPhotoIds();
        PhotoDto photoDto = photosDao.getPhoto(id);
        int currentIdx = ids.indexOf(id);

        if (currentIdx == -1 || Objects.isNull(photoDto)) {
            throw new PhotosBusinessException(MessageConstants.WARN_PHOTO_NOT_FOUND);
        }

        PhotoEntity photoEntity = new PhotoEntity();
        BeanUtils.copyProperties(photoDto, photoEntity);
        photoEntity.setRawPhoto(Base64.getEncoder().encodeToString(photoDto.getRawPhoto()));

        if (Objects.nonNull(photoDto.getShootingDateTime())) {
            photoEntity.setShootingDateTime(
                    StandardConstants.SIMPLE_DATE_FORMAT.format(photoDto.getShootingDateTime()));
        }

        if (ids.size() > currentIdx + 1) {
            photoEntity.setNextId(ids.get(currentIdx + 1));
        }

        if (currentIdx > 0) {
            photoEntity.setPrevId(ids.get(currentIdx - 1));
        }

        return photoEntity;
    }

    @Transactional
    @Override
    public void addPhoto(
            @NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException {

        PhotoDto photoDto = new PhotoDto();
        photoDto.setFileName(sendingPhoto.getOriginalFilename());
        photoDto.setMimeType(sendingPhoto.getContentType());

        try {
            photoDto.setRawPhoto(sendingPhoto.getBytes());
        } catch (Exception e) {
            throw new PhotosBusinessException(MessageConstants.ERROR_FILE_PROCESSING_FAILED);
        }

        String formatName = sendingPhoto.getContentType().replaceAll(".*/", "");
        photoDto.setThumbnail(PhotosUtil.createThumbnail(sendingPhoto, formatName));
        PhotosUtil.setMetaDatum(photoDto, sendingPhoto);
        photosDao.addPhoto(photoDto);
    }

    @Override
    public void deletePhotos(@NonNull int[] ids) {

        photosDao.deletePhotos(ids);
    }
}
