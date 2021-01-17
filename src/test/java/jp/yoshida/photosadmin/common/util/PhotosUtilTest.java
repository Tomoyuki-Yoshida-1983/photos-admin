package jp.yoshida.photosadmin.common.util;

import com.drew.imaging.ImageMetadataReader;
import jp.yoshida.photosadmin.common.constant.MessagesConstants;
import jp.yoshida.photosadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photosadmin.common.exception.PhotosSystemException;
import jp.yoshida.photosadmin.repository.entity.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PhotosUtilTest {

    @Mock
    MockMultipartFile mockSendingPhoto;

    MultipartFile sendingPhoto;

    String originalFileName;

    String fileName;

    String contentType;

    byte[] content;

    Photo photo;

    String origin;

    String appendix;

    String delimiter;

    @BeforeEach
    void setUp() throws IOException {

        Path path = Paths.get("src/test/resources/test-datum/test-sending-photo.jpg");
        fileName = "fileName";
        originalFileName = "originalFileName";
        contentType = "image/jpg";
        content = Files.readAllBytes(path);
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);

        photo = new Photo();

        origin = "origin";
        appendix = "appendix";
        delimiter = "delimiter";
    }

    @Test
    @DisplayName("サムネイル作成_正常系")
    void createThumbnailN1() throws PhotosSystemException, PhotosBusinessException {

        byte[] actual = PhotosUtil.createThumbnail(sendingPhoto);
        assertNotNull(actual);
    }

    @Test
    @DisplayName("サムネイル作成_異常系_一時ファイル作成に失敗")
    void createThumbnailE1() throws IOException {

        doThrow(IOException.class).when(mockSendingPhoto).transferTo(any(Path.class));
        PhotosSystemException e
                = assertThrows(PhotosSystemException.class, () ->PhotosUtil.createThumbnail(mockSendingPhoto));
        assertEquals(MessagesConstants.ERROR_TEMP_FILE_PROCESSING_FAILED, e.getMessage());
    }

    @Test
    @DisplayName("サムネイル作成_異常系_メタデータ取得に失敗")
    void createThumbnailE2() {

        try (MockedStatic<ImageIO> imageIOMock = mockStatic(ImageIO.class)) {
            imageIOMock.when(() -> ImageIO.read(any(FileInputStream.class))).thenThrow(IOException.class);
            PhotosBusinessException e
                    = assertThrows(PhotosBusinessException.class, () -> PhotosUtil.createThumbnail(sendingPhoto));
            assertEquals(MessagesConstants.ERROR_FILE_PROCESSING_FAILED, e.getMessage());
            assertEquals(MessagesConstants.INFO_LEVEL_ERROR, e.getInfoLevel());
        }
    }

    @Test
    @DisplayName("メタデータ設定_正常系")
    void setMetaDatumN1() throws PhotosSystemException, PhotosBusinessException {

        PhotosUtil.setMetaDatum(photo, sendingPhoto);
        assertNotNull(photo.getWidth());
        assertNotNull(photo.getHeight());
        assertNotNull(photo.getShootingDateTime());
        assertNotNull(photo.getLatitude());
        assertNotNull(photo.getLatitudeRef());
        assertNotNull(photo.getLongitude());
        assertNotNull(photo.getLongitudeRef());
    }

    @Test
    @DisplayName("メタデータ設定_正常系_一時ファイル作成に失敗")
    void setMetaDatumE1() throws IOException {

        doThrow(IOException.class).when(mockSendingPhoto).transferTo(any(Path.class));
        PhotosSystemException e
                = assertThrows(PhotosSystemException.class, () -> PhotosUtil.setMetaDatum(photo, mockSendingPhoto));
        assertEquals(MessagesConstants.ERROR_TEMP_FILE_PROCESSING_FAILED, e.getMessage());
    }

    @Test
    @DisplayName("メタデータ設定_異常系_メタデータ取得に失敗")
    void setMetaDatumE2() {

        try (MockedStatic<ImageMetadataReader> imageMetadataReaderMock = mockStatic(ImageMetadataReader.class)) {
            imageMetadataReaderMock.when(() ->
                    ImageMetadataReader.readMetadata(any(File.class))).thenThrow(IOException.class);
            PhotosBusinessException e
                    = assertThrows(PhotosBusinessException.class, () ->PhotosUtil.setMetaDatum(photo, sendingPhoto));
            assertEquals(MessagesConstants.ERROR_FILE_PROCESSING_FAILED, e.getMessage());
            assertEquals(MessagesConstants.INFO_LEVEL_ERROR, e.getInfoLevel());
        }
    }

    @Test
    @DisplayName("文字列結合_正常系_元文字列・追加文字列・区切り文字が全て非NULL")
    void concatIgnoreNullN1() {

        String actual = PhotosUtil.concatIgnoreNull(origin, appendix, delimiter);
        assertEquals(origin + delimiter + appendix, actual);
    }

    @Test
    @DisplayName("文字列結合_正常系_元文字列がNULL")
    void concatIgnoreNullN2() {

        origin = null;
        String actual = PhotosUtil.concatIgnoreNull(origin, appendix, delimiter);
        assertEquals(appendix, actual);
    }

    @Test
    @DisplayName("文字列結合_正常系_追加文字列がNULL")
    void concatIgnoreNullN3() {

        appendix = null;
        String actual = PhotosUtil.concatIgnoreNull(origin, appendix, delimiter);
        assertEquals(origin, actual);
    }

    @Test
    @DisplayName("文字列結合_正常系_区切り文字がNULL")
    void concatIgnoreNullN4() {

        delimiter = null;
        String actual = PhotosUtil.concatIgnoreNull(origin, appendix, delimiter);
        assertEquals(origin + appendix, actual);
    }

}