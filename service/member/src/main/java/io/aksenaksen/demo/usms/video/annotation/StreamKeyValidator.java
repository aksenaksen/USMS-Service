package io.aksenaksen.demo.usms.video.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static io.aksenaksen.demo.usms.video.constant.VideoConstants.STREAM_KEY_PATTERN;


public class StreamKeyValidator  implements ConstraintValidator<StreamKey, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null) return true;

        return Pattern.matches(STREAM_KEY_PATTERN, value);
    }
}
