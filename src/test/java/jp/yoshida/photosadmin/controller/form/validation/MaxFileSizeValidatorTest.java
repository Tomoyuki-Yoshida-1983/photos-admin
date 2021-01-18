package jp.yoshida.photosadmin.controller.form.validation;

import jp.yoshida.photosadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photosadmin.common.constant.MessagesConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MaxFileSizeValidatorTest {

    @MaxFileSize(value = 1, unit = KeyWordsConstants.UNIT_FILE_SIZE.MEGA_BYTE)
    MultipartFile sendingPhoto;

    @MaxFileSize(value = 1, unit = KeyWordsConstants.UNIT_FILE_SIZE.KILO_BYTE)
    MultipartFile overSizeSendingPhoto;

    String originalFileName;

    String fileName;

    String contentType;

    byte[] content;

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("src/test/resources/test-datum/test-sending-photo.jpg");
        fileName = "fileName";
        originalFileName = "originalFileName";
        contentType = "image/jpg";
        content = Files.readAllBytes(path);
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
    }

    @Test
    @DisplayName("ファイルサイズ検証_正常系")
    void isValidN1() {

        Set<ConstraintViolation<MaxFileSizeValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        assertEquals(violations.size(), 0);
    }

    @Test
    @DisplayName("ファイルサイズ検証_正常系_フィールドがNULL")
    void isValidN2() {

        sendingPhoto = null;
        Set<ConstraintViolation<MaxFileSizeValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        assertEquals(violations.size(), 0);
    }

    @Test
    @DisplayName("ファイルサイズ検証_異常系_ファイルサイズ超過")
    void isValidE1() {

        overSizeSendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<MaxFileSizeValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_SIZE_EXCEEDED, actual);
    }
}
