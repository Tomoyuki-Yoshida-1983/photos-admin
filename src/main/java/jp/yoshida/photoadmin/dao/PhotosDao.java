package jp.yoshida.photoadmin.dao;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;

import java.util.List;

public interface PhotosDao {

    @NonNull
    List<PhotoDto> getPhotos();

    void addPhoto(@NonNull PhotoDto photoDto);
}
