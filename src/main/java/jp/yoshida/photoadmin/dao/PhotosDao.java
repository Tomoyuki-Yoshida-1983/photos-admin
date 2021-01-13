package jp.yoshida.photoadmin.dao;

import jp.yoshida.photoadmin.dao.entity.Photo;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public interface PhotosDao {

    @NonNull
    List<Photo> getPhotos();

    @NonNull
    List<Integer> getPhotoIds();

    @Nullable
    Photo getPhoto(@NonNull int id);

    void addPhoto(@NonNull Photo photo);

    int deletePhotos(@NonNull int[] ids);
}
