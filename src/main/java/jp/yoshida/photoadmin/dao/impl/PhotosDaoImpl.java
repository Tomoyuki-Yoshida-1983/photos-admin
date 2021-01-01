package jp.yoshida.photoadmin.dao.impl;

import jp.yoshida.photoadmin.dao.PhotosDao;
import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.mapper.PhotosMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PhotosDaoImpl implements PhotosDao {

    private final PhotosMapper photosMapper;

    @Override
    public void addPhoto(@NonNull PhotoDto photoDto) {

        photosMapper.addPhoto(photoDto);
    }
}
