package io.aksenaksen.demo.usms.constant;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private int code;
    private String message;
}
