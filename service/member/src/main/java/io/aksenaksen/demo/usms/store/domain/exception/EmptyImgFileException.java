package io.aksenaksen.demo.usms.store.domain.exception;

import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.EMPTY_IMG_FILE_CODE;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.EMPTY_IMG_FILE_MESSAGE;


@Getter
public class EmptyImgFileException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto =
            new ErrorResponseDto(EMPTY_IMG_FILE_CODE, EMPTY_IMG_FILE_MESSAGE);

}
