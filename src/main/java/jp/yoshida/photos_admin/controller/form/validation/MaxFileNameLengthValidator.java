package jp.yoshida.photos_admin.controller.form.validation;

import jp.yoshida.photos_admin.common.constant.MessagesConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <p>ファイル名が最大文字数を超えないことを検証するバリデーター</p>
 * <p>ファイル名が最大文字数を超えた場合、検証エラーとする</p>
 * <p>ファイル名がNULLまたは空文字の場合、ファイル名が未入力である旨のエラーメッセージを設定して検証エラーとする</p>
 */
public class MaxFileNameLengthValidator implements ConstraintValidator<MaxFileNameLength, MultipartFile> {

    private int maxFileNameLength;

    @Override
    public void initialize(MaxFileNameLength maxFileNameLength) {

        this.maxFileNameLength = maxFileNameLength.value();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(multipartFile)) {
            return true;
        }

        String fileName = multipartFile.getOriginalFilename();

        if (StringUtils.isEmpty(fileName)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    MessagesConstants.ERROR_FILE_NAME_IS_EMPTY).addConstraintViolation();
            return false;
        }

        return fileName.length() <= this.maxFileNameLength;
    }
}
