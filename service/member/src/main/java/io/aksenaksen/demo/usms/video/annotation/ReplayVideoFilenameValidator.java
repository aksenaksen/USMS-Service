package io.aksenaksen.demo.usms.video.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static io.aksenaksen.demo.usms.video.constant.VideoConstants.REPLAY_VIDEO_FILE_NAME_PATTERN;


public class ReplayVideoFilenameValidator implements ConstraintValidator<ReplayVideoFilename, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null) return true;

        return Pattern.matches(REPLAY_VIDEO_FILE_NAME_PATTERN, value);
    }
}
