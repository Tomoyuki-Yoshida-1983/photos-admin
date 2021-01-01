package jp.yoshida.photoadmin.service;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;

import java.io.IOException;

public interface PhotosService {

    void addPhoto(@NonNull PhotoDto photoDto) throws IOException;
}
