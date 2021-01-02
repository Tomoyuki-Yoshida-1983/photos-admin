package jp.yoshida.photoadmin.mapper;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotosMapper {

    @NonNull
    List<PhotoDto> getPhotos();

    void addPhoto(@NonNull PhotoDto photoDto);
}
