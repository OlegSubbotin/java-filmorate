package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MinReleaseDateValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinReleaseDate {
    String minDate() default "1895-12-28";
    String message() default "Release date error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
