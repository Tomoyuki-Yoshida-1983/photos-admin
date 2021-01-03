package jp.yoshida.photoadmin.mapper;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface PhotosMapper {

    @NonNull
    List<PhotoDto> getPhotos();

    @NonNull
    List<Integer> getPhotoIds();

    @Nullable
    PhotoDto getPhoto(@NonNull int id);

    void addPhoto(@NonNull PhotoDto photoDto);
}
