package jp.yoshida.photoadmin.service;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

public interface PhotosService {

    @NonNull
    List<PhotoDto> getPhotos();

    @Nullable
    PhotoDto getPhoto(@NonNull int id);

    void addPhoto(@NonNull PhotoDto photoDto) throws IOException;
}
