package io.aksenaksen.demo.usms.store.domain.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.UNAVAILABLE_STORE_CODE;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.UNAVAILABLE_STORE_MESSAGE;

@Getter
public class UnavailableStoreException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto = new ErrorResponseDto(UNAVAILABLE_STORE_CODE, UNAVAILABLE_STORE_MESSAGE);
}
