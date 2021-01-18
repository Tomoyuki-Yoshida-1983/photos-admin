package jp.yoshida.photosadmin.controller.form.validation;

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
class PossibleFileNameValidatorTest {

    @PossibleFileName
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
    @DisplayName("使用可能ファイル名検証_正常系")
    void isValidN1() {

        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        assertEquals(violations.size(), 0);
    }

    @Test
    @DisplayName("使用可能ファイル名検証_正常系_フィールドがNULL")
    void isValidN2() {

        sendingPhoto = null;
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        assertEquals(violations.size(), 0);
    }

    @Test
    @DisplayName("使用可能ファイル名検証_正常系_ファイル名がNULL")
    void isValidN3() {

        originalFileName = null;
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        assertEquals(violations.size(), 0);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名にバックスラッシュが含まれる")
    void isValidE1() {

        originalFileName = "original\\FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名にスラッシュが含まれる")
    void isValidE2() {

        originalFileName = "original/FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名にコロンが含まれる")
    void isValidE3() {

        originalFileName = "original:FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名にアスタリスクが含まれる")
    void isValidE4() {

        originalFileName = "original*FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名にクエスチョンが含まれる")
    void isValidE5() {

        originalFileName = "original?FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名にダブルクォーテーションが含まれる")
    void isValidE6() {

        originalFileName = "original\"FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名に小なりが含まれる")
    void isValidE7() {

        originalFileName = "original<FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名に大なりが含まれる")
    void isValidE8() {

        originalFileName = "original>FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }

    @Test
    @DisplayName("ファイル名最大文字数検証_異常系_ファイル名にパイプが含まれる")
    void isValidE9() {

        originalFileName = "original|FileName";
        sendingPhoto = new MockMultipartFile(fileName, originalFileName, contentType, content);
        Set<ConstraintViolation<PossibleFileNameValidatorTest>> violations
                = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        String actual = violations.stream().iterator().next().getMessage();
        assertEquals(MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS, actual);
    }
}
