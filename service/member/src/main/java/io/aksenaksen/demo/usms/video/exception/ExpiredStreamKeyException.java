package io.aksenaksen.demo.usms.video.exception;


import static io.aksenaksen.demo.usms.constant.CustomStatusCode.EXPIRED_STREAM_KEY;

public class ExpiredStreamKeyException extends IllegalStreamKeyException {

    public ExpiredStreamKeyException() {

        super(EXPIRED_STREAM_KEY, "유효하지 않은 스트림 키 값입니다.");
    }
}
