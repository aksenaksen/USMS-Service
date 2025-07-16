package io.aksenaksen.demo.usms.store.domain.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_ALLOWED_IMG_FILE_FORMAT_CODE;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_ALLOWED_IMG_FILE_FORMAT_MESSAGE;


@Getter
public class NotAllowedImgFileFormatException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto =
            new ErrorResponseDto(NOT_ALLOWED_IMG_FILE_FORMAT_CODE, NOT_ALLOWED_IMG_FILE_FORMAT_MESSAGE);

}
