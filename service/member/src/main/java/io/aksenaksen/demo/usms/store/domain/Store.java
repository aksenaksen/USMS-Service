package io.aksenaksen.demo.usms.store.domain;

import io.aksenaksen.demo.usms.store.domain.dto.StoreDto;
import io.aksenaksen.demo.usms.store.adaptor.integration.StoreStateConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(indexes = @Index(name = "usms_store_business_license_img_id_idx",unique = true, columnList = "businessLicenseImgId"))
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String storeName;
    private String storeAddress;
    private String businessLicenseCode;             /* 사업자 등록 번호 */
    private String businessLicenseImgId;
    @Convert(converter = StoreStateConverter.class)
    private StoreState storeState;
    private String adminComment;

    public static Store createOf(StoreDto storeDto , String businessLicenseImgId){
        Store store = new Store();
        store.setUserId(storeDto.getUserId());
        store.setStoreName(storeDto.getName());
        store.setStoreAddress(storeDto.getAddress());
        store.setBusinessLicenseCode(storeDto.getBusinessLicenseCode());
        store.setBusinessLicenseImgId(businessLicenseImgId);
        store.setStoreState(StoreState.READY);
        return store;
    }

    public String makeRegion(){
        String[] address = this.storeAddress.split(" ");
        return address[0] + " " + address[1];
    }

    public void approve(String approvalMessage) {
        this.storeState = StoreState.APPROVAL;
        this.adminComment = approvalMessage;
    }

    public void disapprove(String disapprovalMessage) {
        this.storeState = StoreState.DISAPPROVAL;
        this.adminComment = disapprovalMessage;
    }

    public void lock(String adminComment) {
        this.storeState = StoreState.STOPPED;
        this.adminComment = adminComment;
    }
    public void update(String storeName,
                       String storeAddress,
                       String businessLicenseCode) {

        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.businessLicenseCode = businessLicenseCode;
        this.storeState = StoreState.READY;
    }


}
