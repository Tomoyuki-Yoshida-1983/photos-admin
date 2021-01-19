package jp.yoshida.photos_admin.service.impl;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import jp.yoshida.photos_admin.common.constant.MessagesConstants;
import jp.yoshida.photos_admin.common.constant.StandardsConstants;
import jp.yoshida.photos_admin.common.exception.PhotosBusinessException;
import jp.yoshida.photos_admin.common.exception.PhotosSystemException;
import jp.yoshida.photos_admin.common.util.PhotosUtil;
import jp.yoshida.photos_admin.repository.PhotosRepository;
import jp.yoshida.photos_admin.repository.entity.Photo;
import jp.yoshida.photos_admin.service.PhotosService;
import jp.yoshida.photos_admin.service.dto.PhotoDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

/**
 * 写真の登録・検索・削除をするサービス実装
 */
@Service
@RequiredArgsConstructor
public class PhotosServiceImpl implements PhotosService {

    private final PhotosRepository photosRepository;

    private final Validator validator;

    /**
     * 写真一覧取得
     * @return 写真DTOの一覧
     */
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

    /**
     * 写真詳細取得
     * @param id 検索する写真のID
     * @return 写真DTO
     * @throws PhotosBusinessException 業務例外
     */
    @NonNull
    @Override
    public PhotoDto getPhoto(@NonNull int id) throws PhotosBusinessException {

        // 前の写真と次の写真が存在するかどうか判断するため、写真テーブルに存在する写真のIDの一覧を取得する。
        List<Integer> ids = photosRepository.getPhotoIds();
        Photo photo = photosRepository.getPhoto(id);
        int currentIdx = ids.indexOf(id);

        // IDに一致する写真が存在しない場合、業務例外とする。
        if (currentIdx == -1 || Objects.isNull(photo)) {
            throw new PhotosBusinessException(
                    MessagesConstants.WARN_PHOTO_NOT_FOUND,
                    MessagesConstants.INFO_LEVEL_WARN);
        }

        PhotoDto photoDto = new PhotoDto();
        BeanUtils.copyProperties(photo, photoDto);
        photoDto.setRawPhoto(Base64.getEncoder().encodeToString(photo.getRawPhoto()));

        // 撮影場所の緯度と北緯／南緯区分を文字列結合して1つのフィールドにまとめて返却する。経度も同様に処理する。
        photoDto.setLatitude(PhotosUtil.concatIgnoreNull(
                photo.getLatitude(), photo.getLatitudeRef(), KeyWordsConstants.SYMBOL_SPACE));
        photoDto.setLongitude(PhotosUtil.concatIgnoreNull(
                photo.getLongitude(), photo.getLongitudeRef(), KeyWordsConstants.SYMBOL_SPACE));

        if (Objects.nonNull(photo.getShootingDateTime())) {
            photoDto.setShootingDateTime(
                    StandardsConstants.SIMPLE_DATE_FORMAT.format(photo.getShootingDateTime()));
        }

        // IDの一致する写真の次の写真が存在する場合、次の写真のIDを設定する。
        if (ids.size() > currentIdx + 1) {
            photoDto.setNextId(ids.get(currentIdx + 1));
        }

        // IDの一致する写真の前の写真が存在する場合、前の写真のIDを設定する。
        if (currentIdx > 0) {
            photoDto.setPrevId(ids.get(currentIdx - 1));
        }

        return photoDto;
    }

    /**
     * 写真登録
     * @param sendingPhoto アップロードされた写真ファイル
     * @throws PhotosBusinessException 業務例外
     * @throws PhotosSystemException システム例外
     */
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

        // 写真に設定されたメタデータに不正がある場合、業務例外とする。
        if (violations.size() > 0) {
            throw new PhotosBusinessException(
                    MessagesConstants.ERROR_FILE_PROCESSING_FAILED,
                    MessagesConstants.INFO_LEVEL_ERROR);
        }

        photosRepository.addPhoto(photo);
    }

    /**
     * 写真削除
     * @param ids 削除する写真の配列
     * @throws PhotosBusinessException 業務例外
     */
    @Override
    public void deletePhotos(@NonNull int[] ids) throws PhotosBusinessException {

        // 写真の削除件数が0件の場合、業務例外とする。
        if (photosRepository.deletePhotos(ids) == 0) {
            throw new PhotosBusinessException(
                    MessagesConstants.WARN_PHOTO_NOT_FOUND,
                    MessagesConstants.INFO_LEVEL_WARN);
        }
    }
}
