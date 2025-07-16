package io.aksenaksen.demo.usms.store.domain.exception;

import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import lombok.Getter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_EXISTING_BUSINESS_LICENSE_IMG_KEY_CODE;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_EXISTING_BUSINESS_LICENSE_IMG_KEY_MESSAGE;

@Getter
public class NotExistingBusinessLicenseImgFileKeyException extends RuntimeException {

    private final ErrorResponseDto errorResponseDto =
            new ErrorResponseDto(NOT_EXISTING_BUSINESS_LICENSE_IMG_KEY_CODE, NOT_EXISTING_BUSINESS_LICENSE_IMG_KEY_MESSAGE);

}
