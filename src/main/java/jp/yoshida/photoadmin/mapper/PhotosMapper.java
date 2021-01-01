package jp.yoshida.photoadmin.mapper;

import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PhotosMapper {

    void addPhoto(@NonNull PhotoDto photoDto);
}
