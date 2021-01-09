package jp.yoshida.photoadmin.controller.form.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Constraint(validatedBy = MaxFileSizeGbValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxFileSizeGb {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "{jp.yoshida.photoadmin.controller.form.validation.MaxFileSizeGb.message}";

    int value() default 0;

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        MaxFileSizeGb[] value();
    }
}
