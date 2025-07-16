package io.aksenaksen.demo.usms.video.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static io.aksenaksen.demo.usms.video.constant.VideoConstants.LIVE_VIDEO_FILE_NAME_PATTERN;


public class LiveVideoFilenameValidator implements ConstraintValidator<LiveVideoFilename, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null) return true;

        return Pattern.matches(LIVE_VIDEO_FILE_NAME_PATTERN, value);
    }
}
