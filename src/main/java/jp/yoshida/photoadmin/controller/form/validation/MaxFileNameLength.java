package jp.yoshida.photoadmin.controller.form.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = MaxFileNameLengthValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileNameLength {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "{jp.yoshida.photoadmin.controller.form.validation.MaxFileNameLength.message}";

    int value() default 255;

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        MaxFileNameLength[] value();
    }
}
