package jp.yoshida.photos_admin.controller.form.validation;

import jp.yoshida.photos_admin.common.constant.MessagesConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * ファイル名が最大文字数を超えないことを検証するアノテーション
 */
@Constraint(validatedBy = MaxFileNameLengthValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileNameLength {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default MessagesConstants.ERROR_FILE_NAME_LENGTH_EXCEEDED;

    int value() default 255;

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        MaxFileNameLength[] value();
    }
}
