package jp.yoshida.photosadmin.controller.form.validation;

import jp.yoshida.photosadmin.common.constant.MessagesConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

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
