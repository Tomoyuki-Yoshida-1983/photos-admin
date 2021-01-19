package jp.yoshida.photos_admin.controller.form.validation;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * MIMEタイプが画像ファイルであることを検証するバリデーター
 */
public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {

    @Override
    public void initialize(ImageFile imageFile) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(multipartFile)) {
            return true;
        }

        String mimeType = multipartFile.getContentType();
        return Objects.nonNull(mimeType) && mimeType.matches(KeyWordsConstants.MIME_TYPE_ANY_IMAGE);
    }
}
