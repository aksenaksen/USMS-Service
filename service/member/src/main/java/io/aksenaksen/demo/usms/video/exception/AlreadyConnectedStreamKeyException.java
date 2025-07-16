package io.aksenaksen.demo.usms.video.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.ALREADY_CONNECTED_STREAM_KEY;


@Getter
public class AlreadyConnectedStreamKeyException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto;

    public AlreadyConnectedStreamKeyException() {

        errorResponseDto = new ErrorResponseDto(ALREADY_CONNECTED_STREAM_KEY, "이미 해당 키에 연결된 스트림이 존재합니다.");
    }
}
