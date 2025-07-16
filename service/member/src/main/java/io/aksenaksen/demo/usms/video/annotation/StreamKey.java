package io.aksenaksen.demo.usms.video.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.INVALID_STREAM_KEY_FORMAT_MESSAGE;


@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StreamKeyValidator.class)
public @interface StreamKey {

    String message() default INVALID_STREAM_KEY_FORMAT_MESSAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
