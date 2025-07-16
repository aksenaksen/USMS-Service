package io.aksenaksen.demo.usms.store.domain.dto;

import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.store.domain.StoreState;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

    private Long id;
    private String userId;
    private String name;
    private String address;
    private String businessLicenseCode; /* 사업자 등록 번호 */
    private String businessLicenseImgId;
    private StoreState storeState;
    private String adminComment;

    public StoreDto(Store store) {
        this.id = store.getId();
        this.userId = store.getUserId();
        this.name = store.getStoreName();
        this.address = store.getStoreAddress();
        this.businessLicenseCode = store.getBusinessLicenseCode();
        this.businessLicenseImgId = store.getBusinessLicenseImgId();
        this.storeState = store.getStoreState();
        this.adminComment = store.getAdminComment();
    }

    public static StoreDto of(String userId, HttpRequestCreatingStoreDto request) {
        return StoreDto.builder()
                .userId(userId)
                .name(request.getStoreName())
                .address(request.getStoreAddress())
                .businessLicenseCode(request.getBusinessLicenseCode())
                .build();
    }
}
