package io.aksenaksen.demo.usms.accident.dto;

import io.aksenaksen.demo.usms.accident.repository.AccidentBehavior;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static io.aksenaksen.demo.usms.accident.constant.AccidentConstants.DATE_FORMAT_REGEX;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.*;

@Getter
@Setter
public class HttpRequestRetrievingAccidentDto {

    private List<AccidentBehavior> behavior = new ArrayList<>();

    @Pattern(regexp = DATE_FORMAT_REGEX, message = INVALID_DATE_FORMAT_MESSAGE)
    private String startDate;

    @Pattern(regexp = DATE_FORMAT_REGEX, message = INVALID_DATE_FORMAT_MESSAGE)
    private String endDate;

    @NotNull(message = NOT_ALLOWED_PAGE_OFFSET_FORMAT_MESSAGE)
    @PositiveOrZero(message = NOT_ALLOWED_PAGE_OFFSET_FORMAT_MESSAGE)
    private Integer offset;

    @NotNull(message = NOT_ALLOWED_PAGE_SIZE_FORMAT_MESSAGE)
    @Positive(message = NOT_ALLOWED_PAGE_SIZE_FORMAT_MESSAGE)
    private Integer size;

}
