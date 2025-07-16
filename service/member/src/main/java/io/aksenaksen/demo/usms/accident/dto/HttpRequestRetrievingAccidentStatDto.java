package io.aksenaksen.demo.usms.accident.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static io.aksenaksen.demo.usms.accident.constant.AccidentConstants.DATE_FORMAT_REGEX;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.INVALID_DATE_FORMAT_MESSAGE;

@Getter
@Setter
public class HttpRequestRetrievingAccidentStatDto {

    @Pattern(regexp = DATE_FORMAT_REGEX, message = INVALID_DATE_FORMAT_MESSAGE)
    private String startDate;

    @Pattern(regexp = DATE_FORMAT_REGEX, message = INVALID_DATE_FORMAT_MESSAGE)
    private String endDate;
}
