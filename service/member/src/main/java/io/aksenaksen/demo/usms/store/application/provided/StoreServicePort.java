package io.aksenaksen.demo.usms.store.application.provided;

import io.aksenaksen.demo.usms.store.domain.StoreState;
import io.aksenaksen.demo.usms.store.domain.dto.ImageDto;
import io.aksenaksen.demo.usms.store.domain.dto.StoreDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreServicePort {

    void update(Long storeId,
                String storeName,
                String storeAddress,
                String businessLicenseCode,
                MultipartFile businessLicenseImgFile);

    void changeStoreState(Long storeId, StoreState requestState, String message);

    void delete(Long storeId);

    StoreDto createStore(StoreDto storeDto, MultipartFile businessLicenseImgFile);

    StoreDto find(Long storeId);

    ImageDto findBusinessLicenseImgFile(String businessLicenseImgFileKey);

    List<StoreDto> findAll(String userId, int offset, int size);

    List<StoreDto> findAll(String userId, String businessLicenseCode, StoreState storeState, int offset, int size);

    boolean isAvailable(Long storeId);

    void validateOwnedStore(Long storeId, String userId);

    void validateOwnedBusinessLicenseImgKey(Long storeId, String businessLicenseImgFileKey);
}
