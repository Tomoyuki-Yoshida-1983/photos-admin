package jp.yoshida.photoadmin.dao;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;

public interface PhotosDao {

    void addPhoto(@NonNull PhotoDto photoDto);
}
