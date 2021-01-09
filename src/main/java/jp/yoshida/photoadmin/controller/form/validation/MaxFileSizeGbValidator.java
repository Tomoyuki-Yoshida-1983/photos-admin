package jp.yoshida.photoadmin.controller.form.validation;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class MaxFileSizeGbValidator implements ConstraintValidator<MaxFileSizeGb, MultipartFile> {

    private long maxFileSize;

    @Override
    public void initialize(MaxFileSizeGb maxFileSizeGb) {

        this.maxFileSize = maxFileSizeGb.value() * KeyWordsConstants.MAGNIFICATION_GIGA_BYTE;
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(multipartFile)) {
            return true;
        }

        return multipartFile.getSize() <= this.maxFileSize;
    }
}
