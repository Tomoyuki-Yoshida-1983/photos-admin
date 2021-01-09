package jp.yoshida.photoadmin.controller.form.validation;

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

    String message() default "{jp.yoshida.photoadmin.controller.form.validation.ImageFile.message}";

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        ImageFile[] value();
    }
}
