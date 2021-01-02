package jp.yoshida.photoadmin.service;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;

public interface PhotosService {

    @NonNull
    List<PhotoDto> getPhotos();

    void addPhoto(@NonNull PhotoDto photoDto) throws IOException;
}
