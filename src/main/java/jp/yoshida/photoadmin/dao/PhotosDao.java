package jp.yoshida.photoadmin.dao;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public interface PhotosDao {

    @NonNull
    List<PhotoDto> getPhotos();

    @NonNull
    List<Integer> getPhotoIds();

    @Nullable
    PhotoDto getPhoto(@NonNull int id);

    void addPhoto(@NonNull PhotoDto photoDto);
}
