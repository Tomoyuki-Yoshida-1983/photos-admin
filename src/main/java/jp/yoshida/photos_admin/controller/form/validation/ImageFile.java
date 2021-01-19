package jp.yoshida.photos_admin.controller.form.validation;

import jp.yoshida.photos_admin.common.constant.MessagesConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * MIMEタイプが画像ファイルであることを検証するアノテーション
 */
@Constraint(validatedBy = ImageFileValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFile {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default MessagesConstants.ERROR_FILE_IS_NOT_IMAGE;

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        ImageFile[] value();
    }
}
