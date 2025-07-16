package io.aksenaksen.demo.usms.video.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_MATCHING_STREAM_PROTOCOL_AND_FILE_FORMAT;

@Getter
public class NotMatchingStreamingProtocolAndFileFormatException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto;

    public NotMatchingStreamingProtocolAndFileFormatException(String message) {

        errorResponseDto = new ErrorResponseDto(NOT_MATCHING_STREAM_PROTOCOL_AND_FILE_FORMAT, message);
    }

    @Override
    public String getMessage() {

        return errorResponseDto.getMessage();
    }
}
