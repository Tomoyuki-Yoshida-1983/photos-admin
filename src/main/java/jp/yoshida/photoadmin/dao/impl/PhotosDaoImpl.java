package jp.yoshida.photoadmin.dao.impl;

import jp.yoshida.photoadmin.dao.PhotosDao;
import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.mapper.PhotosMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
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

    @NonNull
    @Override
    public List<Integer> getPhotoIds() {

        return photosMapper.getPhotoIds();
    }

    @Nullable
    @Override
    public PhotoDto getPhoto(@NonNull int id) {

        return photosMapper.getPhoto(id);
    }

    @Override
    public void addPhoto(@NonNull PhotoDto photoDto) {

        photosMapper.addPhoto(photoDto);
    }

    @Override
    public void deletePhotos(@NonNull int[] ids) {

        photosMapper.deletePhotos(ids);
    }
}
