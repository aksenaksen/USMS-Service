package io.aksenaksen.demo.usms.accident.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.INVALID_DATE_FORMAT_CODE;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.INVALID_DATE_FORMAT_MESSAGE;

@Getter
public class InvalidDateFlowException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto = new ErrorResponseDto(INVALID_DATE_FORMAT_CODE, INVALID_DATE_FORMAT_MESSAGE);
}
