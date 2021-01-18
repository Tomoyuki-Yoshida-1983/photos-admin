package jp.yoshida.photos_admin.repository;

import jp.yoshida.photos_admin.repository.entity.Photo;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface PhotosRepository {

    @NonNull
    List<Photo> getPhotos();

    @NonNull
    List<Integer> getPhotoIds();

    @Nullable
    Photo getPhoto(@NonNull int id);

    void addPhoto(@NonNull Photo photo);

    int deletePhotos(@Param("ids") @NonNull int[] ids);
}
