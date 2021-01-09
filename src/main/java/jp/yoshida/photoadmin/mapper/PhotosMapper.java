package jp.yoshida.photoadmin.mapper;

import jp.yoshida.photoadmin.dao.entity.Photo;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface PhotosMapper {

    @NonNull
    List<Photo> getPhotos();

    @NonNull
    List<Integer> getPhotoIds();

    @Nullable
    Photo getPhoto(@NonNull int id);

    void addPhoto(@NonNull Photo photo);

    void deletePhotos(@Param("ids") @NonNull int[] ids);
}
