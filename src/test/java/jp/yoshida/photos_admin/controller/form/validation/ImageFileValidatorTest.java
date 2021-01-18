package jp.yoshida.photos_admin.controller.form.validation;

import jp.yoshida.photos_admin.common.constant.MessagesConstants;
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
class ImageFileValidatorTest {

    @ImageFile
    MultipartFile sendingPhoto;

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
    @DisplayName("イメージファイル検証_正常系")
    void isValidN1() {

        Set<ConstraintViolation<ImageFileValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        assertEquals(violations.size(), 0);
    }

    @Test
    @DisplayName("イメージファイル検証_正常系_フィールドがNULL")
    void isValidN2() {

        sendingPhoto = null;
        Set<ConstraintViolation<ImageFileValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        assertEquals(violations.size(), 0);
    }

    @Test
    @DisplayName("イメージファイル検証_異常系_イメージファイルではない")
    void isValidE1() {

        contentType = "text/plain";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<ImageFileValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_IS_NOT_IMAGE, actual);
    }
}
