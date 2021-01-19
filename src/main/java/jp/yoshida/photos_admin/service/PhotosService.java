package jp.yoshida.photos_admin.service;

import jp.yoshida.photos_admin.service.dto.PhotoDto;
import jp.yoshida.photos_admin.common.exception.PhotosBusinessException;
import jp.yoshida.photos_admin.common.exception.PhotosSystemException;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 写真の登録・検索・削除をするサービス
 */
public interface PhotosService {

    /**
     * 写真一覧取得
     * @return 写真DTOの一覧
     */
    @NonNull
    List<PhotoDto> getPhotos();

    /**
     * 写真詳細取得
     * @param id 検索する写真のID
     * @return 写真DTO
     * @throws PhotosBusinessException 業務例外
     */
    @NonNull
    PhotoDto getPhoto(@NonNull int id) throws PhotosBusinessException;

    /**
     * 写真登録
     * @param sendingPhoto アップロードされた写真ファイル
     * @throws PhotosBusinessException 業務例外
     * @throws PhotosSystemException システム例外
     */
    void addPhoto(@NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException;

    /**
     * 写真削除
     * @param ids 削除する写真の配列
     * @throws PhotosBusinessException 業務例外
     */
    void deletePhotos(@NonNull int[] ids) throws PhotosBusinessException;
}
