package io.aksenaksen.demo.usms.video.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_ALLOWED_STREAM_PROTOCOL;


@Getter
public class NotAllowedStreamingProtocolException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto;

    public NotAllowedStreamingProtocolException(String message) {

        errorResponseDto = new ErrorResponseDto(NOT_ALLOWED_STREAM_PROTOCOL, message);
    }

    @Override
    public String getMessage() {

        return errorResponseDto.getMessage();
    }
}
