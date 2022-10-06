package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MinReleaseDateValidator implements ConstraintValidator<MinReleaseDate, LocalDate> {
    private String minDate;
    public static final String PATTERN = "yyyy-MM-dd";
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate date = LocalDate.parse(minDate, DateTimeFormatter.ofPattern(PATTERN));
        return value.isAfter(date);
    }

    @Override
    public void initialize(MinReleaseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minDate = constraintAnnotation.minDate();
    }
}
