package io.aksenaksen.demo.usms.video.exception;


import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_EXISTING_STREAM_KEY;

public class NotExistingStreamKeyException extends IllegalStreamKeyException {

    public NotExistingStreamKeyException() {

        super(NOT_EXISTING_STREAM_KEY, "유효하지 않은 스트림 키 값입니다.");
    }
}
