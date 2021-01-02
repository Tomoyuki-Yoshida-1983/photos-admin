package jp.yoshida.photoadmin.dao.impl;

import jp.yoshida.photoadmin.dao.PhotosDao;
import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.mapper.PhotosMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotosDaoImpl implements PhotosDao {

    private final PhotosMapper photosMapper;

    @NonNull
    @Override
    public List<PhotoDto> getPhotos() {

        return photosMapper.getPhotos();
    }

    @Override
    public void addPhoto(@NonNull PhotoDto photoDto) {

        photosMapper.addPhoto(photoDto);
    }
}
