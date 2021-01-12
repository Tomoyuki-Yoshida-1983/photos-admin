package jp.yoshida.photoadmin.controller.form.validation;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photoadmin.common.constant.MessagesConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = MaxFileSizeValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSize {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default MessagesConstants.ERROR_FILE_SIZE_EXCEEDED;

    int value() default 0;

    KeyWordsConstants.UNIT_FILE_SIZE unit() default KeyWordsConstants.UNIT_FILE_SIZE.BYTE;

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        MaxFileSize[] value();
    }
}