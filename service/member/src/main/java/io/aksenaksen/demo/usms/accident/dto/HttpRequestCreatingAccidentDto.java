package io.aksenaksen.demo.usms.accident.dto;

import io.aksenaksen.demo.usms.accident.repository.AccidentBehavior;
import io.aksenaksen.demo.usms.video.annotation.StreamKey;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.*;

@Getter
@Setter
public class HttpRequestCreatingAccidentDto {

    @NotNull(message = INVALID_STREAM_KEY_FORMAT_MESSAGE)
    @StreamKey
    private String streamKey;

    @NotNull(message = NOT_EXISTING_ACCIDENT_BEHAVIOR_MESSAGE)
    private AccidentBehavior behavior;

    @NotNull(message = INVALID_TIMESTAMP_FORMAT_MESSAGE)
    @Positive(message = INVALID_TIMESTAMP_FORMAT_MESSAGE)
    private Long startTimestamp;
}
