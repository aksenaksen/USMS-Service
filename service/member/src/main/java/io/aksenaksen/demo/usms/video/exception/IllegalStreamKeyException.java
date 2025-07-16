package io.aksenaksen.demo.usms.video.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

@Getter
public abstract class IllegalStreamKeyException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto;

    public IllegalStreamKeyException(String message) {

        errorResponseDto = new ErrorResponseDto(400, message);
    }

    public IllegalStreamKeyException(int code, String message) {

        errorResponseDto = new ErrorResponseDto(code, message);
    }

    @Override
    public String getMessage() {

        return errorResponseDto.getMessage();
    }
}
