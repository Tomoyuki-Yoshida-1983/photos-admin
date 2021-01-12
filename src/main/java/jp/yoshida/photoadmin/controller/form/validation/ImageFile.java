package jp.yoshida.photoadmin.controller.form.validation;

import jp.yoshida.photoadmin.common.constant.MessagesConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


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
