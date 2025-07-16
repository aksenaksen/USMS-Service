package io.aksenaksen.demo.usms.video.handler;

import io.aksenaksen.demo.usms.cctv.exception.NotExistingCctvException;
import io.aksenaksen.demo.usms.cctv.exception.NotOwnedCctvException;
import io.aksenaksen.demo.usms.constant.ErrorResponseDto;
import io.aksenaksen.demo.usms.store.domain.exception.NotOwnedStoreException;
import io.aksenaksen.demo.usms.store.domain.exception.UnavailableStoreException;
import io.aksenaksen.demo.usms.video.adaptor.webapi.StreamKeyController;
import io.aksenaksen.demo.usms.video.adaptor.webapi.VideoController;
import io.aksenaksen.demo.usms.video.exception.AlreadyConnectedStreamKeyException;
import io.aksenaksen.demo.usms.video.exception.IllegalStreamKeyException;
import io.aksenaksen.demo.usms.video.exception.NotAllowedStreamingProtocolException;
import io.aksenaksen.demo.usms.video.exception.NotMatchingStreamingProtocolAndFileFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes = {VideoController.class, StreamKeyController.class})
public class VideoExceptionHandler {

    @ExceptionHandler(IllegalStreamKeyException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStreamKeyException(IllegalStreamKeyException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler(NotAllowedStreamingProtocolException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalStreamingProtocolException(NotAllowedStreamingProtocolException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler(NotMatchingStreamingProtocolAndFileFormatException.class)
    public ResponseEntity<ErrorResponseDto> handleNotMatchingStreamingProtocolAndFileFormatException(NotMatchingStreamingProtocolAndFileFormatException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException exception) {

        String errorMessage = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList())
                .get(0);

        log.error("Exception [Err_Location] : {}", exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponseDto.builder().code(400).message(errorMessage).build()
                );
    }

    @ExceptionHandler(AlreadyConnectedStreamKeyException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyConnectedStreamKeyException(AlreadyConnectedStreamKeyException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler({NotOwnedStoreException.class})
    public ResponseEntity<ErrorResponseDto> handleNotOwnedStoreException(NotOwnedStoreException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace().length == 0 ? exception.getClass() : exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getErrorResponseDto());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler({UnavailableStoreException.class})
    public ResponseEntity<ErrorResponseDto> handleUnavailableStoreException(UnavailableStoreException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace().length == 0 ? exception.getClass() : exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getErrorResponseDto());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler({NotExistingCctvException.class})
    public ResponseEntity<ErrorResponseDto> handleNotExistingStoreException(NotExistingCctvException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace().length == 0 ? exception.getClass() : exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getErrorResponseDto());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler({NotOwnedCctvException.class})
    public ResponseEntity<ErrorResponseDto> handleNotOwnedStoreException(NotOwnedCctvException exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace().length == 0 ? exception.getClass() : exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getErrorResponseDto());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getErrorResponseDto());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAnyOtherException(Exception exception) {

        log.error("Exception [Err_Location] : {}", exception.getStackTrace()[0]);
        log.error("Exception [Err_Msg] : {}", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 에러가 발생했습니다."));
    }
}
