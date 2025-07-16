package io.aksenaksen.demo.usms.store.application.provided;


import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.store.domain.StoreState;
import io.aksenaksen.demo.usms.store.domain.dto.ImageDto;
import io.aksenaksen.demo.usms.store.domain.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService implements StoreServicePort{

    private final StoreCommandService storeCommandService;
    private final StoreQueryService storeQueryService;
    private final ImageQueryService imageQueryService;

    @Transactional
    public void update(Long storeId,
                       String storeName,
                       String storeAddress,
                       String businessLicenseCode,
                       MultipartFile businessLicenseImgFile) {

        storeCommandService.update(storeId,storeName,storeAddress,businessLicenseCode,businessLicenseImgFile);
    }

    @Transactional
    public void changeStoreState(Long storeId, StoreState requestState, String message) {

        storeCommandService.changeStoreState(storeId, requestState, message);
    }

    @Transactional
    public void delete(Long storeId) {

        storeCommandService.delete(storeId);
    }

    @Transactional
    public StoreDto createStore(StoreDto storeDto, MultipartFile businessLicenseImgFile) {

        Store store = storeCommandService.createStore(storeDto, businessLicenseImgFile);
        return new StoreDto(store);
    }

    @Transactional(readOnly = true)
    public StoreDto find(Long storeId) {

        return new StoreDto(storeQueryService.findById(storeId));
    }

    @Transactional(readOnly = true)
    public ImageDto findBusinessLicenseImgFile(String businessLicenseImgFileKey) {

        return imageQueryService.findBusinessLicenseImgFile(businessLicenseImgFileKey);
    }

    @Transactional(readOnly = true)
    public List<StoreDto> findAll(String userId, int offset, int size) {

        return storeQueryService.findAllByUserId(userId,offset,size)
                .stream()
                .map(StoreDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreDto> findAll(String userId, String businessLicenseCode, StoreState storeState, int offset, int size) {

        return storeQueryService.findAll(userId,businessLicenseCode,storeState,offset,size)
                .stream()
                .map(StoreDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isAvailable(Long storeId) {

        return storeQueryService.isAvailable(storeId);
    }


//    @Transactional(readOnly = true)
//    public void validateStore(Long storeId) {
//        Store store = storeRepository.findById(storeId);
//        if(store == null) {
//            throw new NotExistingStoreException();
//        }
//    }

    @Transactional(readOnly = true)
    public void validateOwnedStore(Long storeId, String userId) {

        storeQueryService.validateOwnedStore(storeId,userId);
    }

    @Transactional(readOnly = true)
    public void validateOwnedBusinessLicenseImgKey(Long storeId, String businessLicenseImgFileKey) {

        storeQueryService.validateOwnedBusinessLicenseImgKey(storeId, businessLicenseImgFileKey);
    }

    @Transactional(readOnly = true)
    public String getRegion(Long storeId){

        return storeQueryService.findById(storeId).makeRegion();
    }


}
