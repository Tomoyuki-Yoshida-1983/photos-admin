package jp.yoshida.photoadmin.service.impl;

import jp.yoshida.photoadmin.dao.impl.PhotosDaoImpl;
import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.service.PhotosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotosServiceImpl implements PhotosService {

    private final PhotosDaoImpl photosDaoImpl;

    @Transactional
    @Override
    public void addPhoto(@NonNull PhotoDto photoDto) throws IOException {

        photoDto.setDbRawPhoto(photoDto.getRawPhoto().getBytes());
        photosDaoImpl.addPhoto(photoDto);
    }
}
