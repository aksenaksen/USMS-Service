package io.aksenaksen.demo.usms.accident.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_EXISTING_ACCIDENT_BEHAVIOR_CODE;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_EXISTING_ACCIDENT_BEHAVIOR_MESSAGE;

@Getter
public class NotExistingAccidentBehaviorException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto = new ErrorResponseDto(NOT_EXISTING_ACCIDENT_BEHAVIOR_CODE, NOT_EXISTING_ACCIDENT_BEHAVIOR_MESSAGE);
}
