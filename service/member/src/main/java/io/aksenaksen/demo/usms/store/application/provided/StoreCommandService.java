package io.aksenaksen.demo.usms.store.application.provided;

import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.store.domain.StoreState;
import io.aksenaksen.demo.usms.store.domain.dto.StoreDto;
import io.aksenaksen.demo.usms.store.application.required.ImageRepository;
import io.aksenaksen.demo.usms.store.application.required.StoreRepository;
import io.aksenaksen.demo.usms.store.domain.exception.NotExistingStoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StoreCommandService {

    private final StoreRepository storeRepository;
    private final ImageRepository imageRepository;

    public Store createStore(StoreDto storeDto, MultipartFile businessLicenseImgFile) {

        // 매장 메타데이터 저장
        String fileFormat = businessLicenseImgFile.getOriginalFilename().split("[.]")[1];
        String businessLicenseImgId = UUID.randomUUID().toString().replace("-", "") + "." + fileFormat;

        Store store = Store.createOf(storeDto, businessLicenseImgId);

        storeRepository.save(store);
        // 이미지 파일 저장
        imageRepository.save(businessLicenseImgId, businessLicenseImgFile);

        return store;
    }

    public void update(Long storeId,
                       String storeName,
                       String storeAddress,
                       String businessLicenseCode,
                       MultipartFile businessLicenseImgFile) {


        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotExistingStoreException::new);
        store.update(storeName, storeAddress, businessLicenseCode);
        storeRepository.update(store);

        imageRepository.save(store.getBusinessLicenseImgId(), businessLicenseImgFile);
    }

    public void changeStoreState(Long storeId, StoreState requestState, String message) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotExistingStoreException::new);
        if(requestState == StoreState.APPROVAL){
            store.approve(message);
        }
        else if(requestState == StoreState.DISAPPROVAL){
            store.disapprove(message);
        }
        else if(requestState == StoreState.STOPPED) {
            store.lock(message);
        }
    }

    public void delete(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotExistingStoreException::new);
        storeRepository.delete(store);
    }
}
