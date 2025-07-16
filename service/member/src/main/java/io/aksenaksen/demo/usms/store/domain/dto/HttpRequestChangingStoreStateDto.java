package io.aksenaksen.demo.usms.store.domain.dto;

import io.aksenaksen.demo.usms.store.domain.StoreState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpRequestChangingStoreStateDto {

    private StoreState state;
    private String message;
}
