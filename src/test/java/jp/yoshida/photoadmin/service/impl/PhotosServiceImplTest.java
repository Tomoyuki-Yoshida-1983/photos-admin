package jp.yoshida.photoadmin.service.impl;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photoadmin.common.constant.MessagesConstants;
import jp.yoshida.photoadmin.common.constant.StandardsConstants;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.util.PhotosUtil;
import jp.yoshida.photoadmin.dao.PhotosDao;
import jp.yoshida.photoadmin.dao.entity.Photo;
import jp.yoshida.photoadmin.service.dto.PhotoDto;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PhotosServiceImplTest {

    @InjectMocks
    PhotosServiceImpl photosService;

    @Mock
    PhotosDao photosDao;

    @Mock
    ValidatorImpl validator;

    PhotoDto photoDto1;

    PhotoDto photoDto2;

    List<PhotoDto> photoDtos;

    Photo photo1;

    Photo photo2;

    List<Photo> photos;

    final Integer id1 = 102;
    final Integer nextId1 = 103;
    final Integer prevId1 = 101;
    final byte[] rawPhoto1 = new byte[] {1, 0, 0, 0, 1};
    final String rawPhotoStr1 = Base64.getEncoder().encodeToString(rawPhoto1);
    final byte[] thumbnail1 = new byte[] {1, 0, 0, 0, 2};
    final String thumbnailStr1 = Base64.getEncoder().encodeToString(thumbnail1);
    final String fileName1 = "fileName1";
    final String mimeType1 = "mymeType1";
    final String width1 = "width1";
    final String height1 = "height1";
    final Date shootingDateTime1 = StandardsConstants.SIMPLE_DATE_FORMAT.parse("2021/01/02 12:34:56");
    final String shootingDateTimeStr1 = StandardsConstants.SIMPLE_DATE_FORMAT.format(shootingDateTime1);
    final String latitude1 = "latitude1";
    final String latitudeRef1 = "latitudeRef1";
    final String longitude1 = "longitude1";
    final String longitudeRef1 = "longitudeRef1";

    final Integer id2 = 202;
    final Integer nextId2 = 203;
    final Integer prevId2 = 201;
    final byte[] rawPhoto2 = new byte[] {2, 10, 50, 100, 1};
    final String rawPhotoStr2 = Base64.getEncoder().encodeToString(rawPhoto2);
    final byte[] thumbnail2 = new byte[] {2, 10, 50, 100, 2};
    final String thumbnailStr2 = Base64.getEncoder().encodeToString(thumbnail2);
    final String fileName2 = "fileName2";
    final String mimeType2 = "mymeType2";
    final String width2 = "width2";
    final String height2 = "height2";
    final Date shootingDateTime2 = StandardsConstants.SIMPLE_DATE_FORMAT.parse("2022/01/02 12:34:56");
    final String shootingDateTimeStr2 = StandardsConstants.SIMPLE_DATE_FORMAT.format(shootingDateTime2);
    final String latitude2 = "latitude2";
    final String latitudeRef2 = "latitudeRef2";
    final String longitude2 = "longitude2";
    final String longitudeRef2 = "longitudeRef2";

    PhotosServiceImplTest() throws ParseException {
    }

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(photosService);

        photoDto1 = new PhotoDto();
        photoDto1.setId(id1);
        photoDto1.setNextId(nextId1);
        photoDto1.setPrevId(prevId1);
        photoDto1.setRawPhoto(rawPhotoStr1);
        photoDto1.setThumbnail(thumbnailStr1);
        photoDto1.setFileName(fileName1);
        photoDto1.setMimeType(mimeType1);
        photoDto1.setWidth(width1);
        photoDto1.setHeight(height1);
        photoDto1.setShootingDateTime(shootingDateTimeStr1);
        photoDto1.setLatitude(PhotosUtil.concatIgnoreNull(latitude1, latitudeRef1, KeyWordsConstants.SYMBOL_SPACE));
        photoDto1.setLongitude(PhotosUtil.concatIgnoreNull(longitude1, longitudeRef1, KeyWordsConstants.SYMBOL_SPACE));

        photoDto2 = new PhotoDto();
        photoDto2.setId(id2);
        photoDto2.setNextId(nextId2);
        photoDto2.setPrevId(prevId2);
        photoDto2.setRawPhoto(rawPhotoStr2);
        photoDto2.setThumbnail(thumbnailStr2);
        photoDto2.setFileName(fileName2);
        photoDto2.setMimeType(mimeType2);
        photoDto2.setWidth(width2);
        photoDto2.setHeight(height2);
        photoDto2.setShootingDateTime(shootingDateTimeStr2);
        photoDto2.setLatitude(PhotosUtil.concatIgnoreNull(latitude2, latitudeRef2, KeyWordsConstants.SYMBOL_SPACE));
        photoDto2.setLongitude(PhotosUtil.concatIgnoreNull(longitude2, longitudeRef2, KeyWordsConstants.SYMBOL_SPACE));

        photoDtos = new ArrayList<>();
        photoDtos.add(photoDto1);
        photoDtos.add(photoDto2);

        photo1 = new Photo();
        photo1.setId(id1);
        photo1.setRawPhoto(rawPhoto1);
        photo1.setThumbnail(thumbnail1);
        photo1.setFileName(fileName1);
        photo1.setMimeType(mimeType1);
        photo1.setWidth(width1);
        photo1.setHeight(height1);
        photo1.setShootingDateTime(shootingDateTime1);
        photo1.setLatitude(latitude1);
        photo1.setLatitudeRef(latitudeRef1);
        photo1.setLongitude(longitude1);
        photo1.setLongitudeRef(longitudeRef1);

        photo2 = new Photo();
        photo2.setId(id2);
        photo2.setRawPhoto(rawPhoto2);
        photo2.setThumbnail(thumbnail2);
        photo2.setFileName(fileName2);
        photo2.setMimeType(mimeType2);
        photo2.setWidth(width2);
        photo2.setHeight(height2);
        photo2.setShootingDateTime(shootingDateTime2);
        photo2.setLatitude(latitude2);
        photo2.setLatitudeRef(latitudeRef2);
        photo2.setLongitude(longitude2);
        photo2.setLongitudeRef(longitudeRef2);

        photos = new ArrayList<>();
        photos.add(photo1);
        photos.add(photo2);
    }

    @Test
    @DisplayName("写真一覧取得_正常系")
    void getPhotosN1() {

        // 期待結果のオブジェクトを試験項目に合わせて編集
        photoDto1.setPrevId(null);
        photoDto1.setNextId(null);
        photoDto1.setRawPhoto(null);

        photoDto2.setPrevId(null);
        photoDto2.setNextId(null);
        photoDto2.setRawPhoto(null);

        doReturn(photos).when(photosDao).getPhotos();
        List<PhotoDto> actual = photosService.getPhotos();
        assertEquals(photoDtos, actual);
    }

    @Test
    @DisplayName("写真詳細取得_正常系")
    void getPhotoN1() throws PhotosBusinessException {

        // 期待結果のオブジェクトを試験項目に合わせて編集
        photoDto1.setThumbnail(null);

        List<Integer> ids = Arrays.asList(101, 102, 103);
        doReturn(ids).when(photosDao).getPhotoIds();
        doReturn(photo1).when(photosDao).getPhoto(id1);
        PhotoDto actual = photosService.getPhoto(id1);
        assertEquals(photoDto1, actual);
    }

    @Test
    @DisplayName("写真詳細取得_異常系_検索結果0件")
    void getPhotoE1() {

        List<Integer> ids = Arrays.asList(101, 102, 103);
        int id = 0;
        doReturn(ids).when(photosDao).getPhotoIds();
        doReturn(null).when(photosDao).getPhoto(id);
        PhotosBusinessException e = assertThrows(PhotosBusinessException.class, () -> photosService.getPhoto(id));
        assertEquals(MessagesConstants.WARN_PHOTO_NOT_FOUND, e.getMessage());
        assertEquals(MessagesConstants.INFO_LEVEL_WARN, e.getInfoLevel());
    }

    @Test
    @DisplayName("写真登録_正常系")
    void addPhotoN1() throws IOException {

        Photo photo = new Photo();
        photo.setFileName(fileName1);
        photo.setMimeType(mimeType1);
        photo.setRawPhoto(rawPhoto1);
        photo.setThumbnail(thumbnail1);
        MultipartFile sendingPhoto = mock(MockMultipartFile.class);
        doReturn(fileName1).when(sendingPhoto).getOriginalFilename();
        doReturn(mimeType1).when(sendingPhoto).getContentType();
        doReturn(rawPhoto1).when(sendingPhoto).getBytes();
        doReturn(new HashSet<>()).when(validator).validate(eq(photo));
        try (MockedStatic<PhotosUtil> photosUtil = mockStatic(PhotosUtil.class)) {
            photosUtil.when(() -> PhotosUtil.createThumbnail(sendingPhoto)).thenReturn(thumbnail1);
            doNothing().when(photosDao).addPhoto(photo);
        }
    }

    @Test
    @DisplayName("写真登録_異常系_写真の変換に失敗")
    void addPhotoE1() throws IOException {

        MultipartFile sendingPhoto = mock(MockMultipartFile.class);
        doReturn(fileName1).when(sendingPhoto).getOriginalFilename();
        doReturn(mimeType1).when(sendingPhoto).getContentType();
        doThrow(IOException.class).when(sendingPhoto).getBytes();
        PhotosBusinessException e
                = assertThrows(PhotosBusinessException.class, () -> photosService.addPhoto(sendingPhoto));
        assertEquals(MessagesConstants.ERROR_FILE_PROCESSING_FAILED, e.getMessage());
        assertEquals(MessagesConstants.INFO_LEVEL_ERROR, e.getInfoLevel());
    }

    @Test
    @DisplayName("写真登録_異常系_メタデータのバリデーションチェックエラー")
    void addPhotoE2() throws IOException {

        Photo photo = new Photo();
        photo.setFileName(fileName1);
        photo.setMimeType(mimeType1);
        photo.setRawPhoto(rawPhoto1);
        photo.setThumbnail(thumbnail1);
        MultipartFile sendingPhoto = mock(MockMultipartFile.class);
        doReturn(fileName1).when(sendingPhoto).getOriginalFilename();
        doReturn(mimeType1).when(sendingPhoto).getContentType();
        doReturn(rawPhoto1).when(sendingPhoto).getBytes();
        HashSet<Object> hashSet = new HashSet<>();
        hashSet.add(new Object());
        doReturn(hashSet).when(validator).validate(eq(photo));
        PhotosBusinessException e
                = assertThrows(PhotosBusinessException.class, () -> photosService.addPhoto(sendingPhoto));
        assertEquals(MessagesConstants.ERROR_FILE_PROCESSING_FAILED, e.getMessage());
        assertEquals(MessagesConstants.INFO_LEVEL_ERROR, e.getInfoLevel());
    }

    @Test
    @DisplayName("写真削除_正常系")
    void deletePhotoN1() throws IOException {

        int[] ids = {101, 102, 103};
        doReturn(3).when(photosDao).deletePhotos(ids);
        assertDoesNotThrow(() -> photosService.deletePhotos(ids));
    }

    @Test
    @DisplayName("写真削除_異常系_削除件数が0件")
    void deletePhotoE1() throws IOException {

        int[] ids = {101, 102, 103};
        doReturn(0).when(photosDao).deletePhotos(ids);
        PhotosBusinessException e
                = assertThrows(PhotosBusinessException.class, () -> photosService.deletePhotos(ids));
        assertEquals(MessagesConstants.WARN_PHOTO_NOT_FOUND, e.getMessage());
        assertEquals(MessagesConstants.INFO_LEVEL_WARN, e.getInfoLevel());
    }

}
