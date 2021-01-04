package jp.yoshida.photoadmin.service.impl;

import com.drew.imaging.ImageProcessingException;
import jp.yoshida.photoadmin.PhotoEntity;
import jp.yoshida.photoadmin.common.constant.NameConstants;
import jp.yoshida.photoadmin.common.constant.StandardConstants;
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

import java.io.IOException;
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
    public PhotoEntity getPhoto(@NonNull int id) {

        List<Integer> ids = photosDao.getPhotoIds();
        PhotoDto photoDto = photosDao.getPhoto(id);

        int currentIdx = ids.indexOf(id);
        PhotoEntity photoEntity = new PhotoEntity();
        BeanUtils.copyProperties(photoDto, photoEntity);
        photoEntity.setRawPhoto(Base64.getEncoder().encodeToString(photoDto.getRawPhoto()));

        if (Objects.nonNull(photoDto.getShootingDateTime())) {
            photoEntity.setShootingDateTime(
                    StandardConstants.SIMPLE_DATE_FORMAT.format(photoDto.getShootingDateTime()));
        }

        if (currentIdx >= 0 && ids.size() > currentIdx + 1) {
            photoEntity.setNextId(ids.get(currentIdx + 1));
        }

        if (currentIdx > 0) {
            photoEntity.setPrevId(ids.get(currentIdx - 1));
        }

        return photoEntity;
    }

    @Transactional
    @Override
    public void addPhoto(@NonNull MultipartFile sendingPhoto) throws IOException, ImageProcessingException {

        String fileName = sendingPhoto.getOriginalFilename();
        PhotoDto photoDto = new PhotoDto();
        photoDto.setFileName(fileName);
        String extension = fileName.substring(fileName.lastIndexOf(NameConstants.SYMBOL_DOT));

        switch (extension.toLowerCase()) {
            case NameConstants.EXTENSION_DOT_JPG:
            case NameConstants.EXTENSION_DOT_JPEG:
            case NameConstants.EXTENSION_DOT_JPE:
            case NameConstants.EXTENSION_DOT_JFIF:
                photoDto.setExtension(NameConstants.EXTENSION_JPEG);
                break;
            case NameConstants.EXTENSION_DOT_PNG:
                photoDto.setExtension(NameConstants.EXTENSION_PNG);
                break;
            default:
                break;
        }

        photoDto.setThumbnail(PhotosUtil.createThumbnail(sendingPhoto, photoDto.getExtension()));
        photoDto.setRawPhoto(sendingPhoto.getBytes());
        PhotosUtil.setMetaDatum(photoDto, sendingPhoto);
        photosDao.addPhoto(photoDto);
    }

    @Override
    public void deletePhotos(@NonNull int[] ids) {

        photosDao.deletePhotos(ids);
    }
}
