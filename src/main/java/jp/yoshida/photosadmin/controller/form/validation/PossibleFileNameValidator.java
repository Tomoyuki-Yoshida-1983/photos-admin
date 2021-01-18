package jp.yoshida.photosadmin.controller.form.validation;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PossibleFileNameValidator implements ConstraintValidator<PossibleFileName, MultipartFile> {

    @Override
    public void initialize(PossibleFileName possibleFileName) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(multipartFile)) {
            return true;
        }

        String fileName = multipartFile.getOriginalFilename();

        if (StringUtils.isEmpty(fileName)) {
            return true;
        }

        return !fileName.matches("^.*[\\\\/:*?\"<>|].*$");
    }
}
