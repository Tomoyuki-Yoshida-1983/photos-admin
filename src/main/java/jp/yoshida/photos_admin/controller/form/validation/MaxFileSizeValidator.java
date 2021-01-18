package jp.yoshida.photos_admin.controller.form.validation;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {

    private long value;

    private KeyWordsConstants.UNIT_FILE_SIZE unit;

    @Override
    public void initialize(MaxFileSize maxFileSize) {

        this.value = maxFileSize.value();
        this.unit = maxFileSize.unit();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(multipartFile)) {
            return true;
        }

        return multipartFile.getSize() <= this.value * this.unit.getMagnification();
    }
}
