package io.aksenaksen.demo.usms.store.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private String contentType;
    private long contentLength;
    private byte[] content;
}
