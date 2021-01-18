package jp.yoshida.photosadmin.controller.form.validation;

import jp.yoshida.photosadmin.common.constant.MessagesConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = PossibleFileNameValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PossibleFileName {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default MessagesConstants.ERROR_FILE_NAME_CONTAINS_FORBIDDEN_CHARACTERS;

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        PossibleFileName[] value();
    }
}
