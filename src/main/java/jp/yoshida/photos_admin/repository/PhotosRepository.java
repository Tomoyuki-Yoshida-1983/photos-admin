package jp.yoshida.photos_admin.repository;

import jp.yoshida.photos_admin.repository.entity.Photo;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 写真の登録・検索・削除をするリポジトリ
 */
@Mapper
public interface PhotosRepository {

    /**
     * 写真一覧取得
     * @return 写真の一覧
     */
    @NonNull
    List<Photo> getPhotos();

    /**
     * 写真ID一覧取得
     * @return 写真テーブルに存在する写真のIDの一覧
     */
    @NonNull
    List<Integer> getPhotoIds();

    /**
     * 写真詳細取得
     * @param id 検索する写真のID
     * @return 写真DTO
     */
    @Nullable
    Photo getPhoto(@NonNull int id);

    /**
     * 写真登録
     * @param photo 登録する写真
     */
    void addPhoto(@NonNull Photo photo);

    /**
     * 写真詳細取得
     * @param ids 削除する写真のIDの配列
     * @return 削除件数
     */
    int deletePhotos(@Param("ids") @NonNull int[] ids);
}
