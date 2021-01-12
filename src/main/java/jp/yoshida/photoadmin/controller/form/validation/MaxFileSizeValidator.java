package jp.yoshida.photoadmin.controller.form.validation;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {

    private int value;

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
