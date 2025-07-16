package io.aksenaksen.demo.usms.store.domain.dto;


import io.aksenaksen.demo.usms.store.domain.StoreState;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.*;
import static io.aksenaksen.demo.usms.store.constant.StoreConstants.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequestRetrievingStoreDto {

    private String user;

    private StoreState storeState;

    @Pattern(regexp = BUSINESS_LICENSE_CODE_REGEX, message = INVALID_BUSINESS_LICENSE_CODE_FORMAT_MESSAGE)
    private String businessLicenseCode;

    @NotNull(message = NOT_ALLOWED_PAGE_OFFSET_FORMAT_MESSAGE)
    @PositiveOrZero(message = NOT_ALLOWED_PAGE_OFFSET_FORMAT_MESSAGE)
    private Integer offset;

    @NotNull(message = NOT_ALLOWED_PAGE_SIZE_FORMAT_MESSAGE)
    @Positive(message = NOT_ALLOWED_PAGE_SIZE_FORMAT_MESSAGE)
    private Integer size;

}
