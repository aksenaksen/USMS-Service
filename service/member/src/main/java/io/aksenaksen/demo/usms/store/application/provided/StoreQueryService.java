package io.aksenaksen.demo.usms.store.application.provided;

import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.store.domain.StoreState;
import io.aksenaksen.demo.usms.store.domain.exception.NotExistingStoreException;
import io.aksenaksen.demo.usms.store.domain.exception.NotOwnedBusinessLicenseImgIdException;
import io.aksenaksen.demo.usms.store.domain.exception.NotOwnedStoreException;
import io.aksenaksen.demo.usms.store.application.required.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreQueryService {

    private final StoreRepository storeRepository;

    public Store findById(Long storeId) {

        return storeRepository.findById(storeId)
                .orElseThrow(NotExistingStoreException::new);
    }

    public List<Store> findAllByUserId(String userId, int offset, int size) {

        return storeRepository.findByUserId(userId, offset, size);
    }

    public List<Store> findAll(String userId, String businessLicenseCode, StoreState storeState, int offset, int size) {

        return storeRepository.findAll(userId, businessLicenseCode, storeState, offset, size);
    }

    public boolean isAvailable(Long storeId) {

        return storeRepository.findById(storeId)
                .orElseThrow(NotExistingStoreException::new)
                .getStoreState() == StoreState.APPROVAL;
    }

    public void validateOwnedStore(Long storeId, String userId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotExistingStoreException::new);
        if(!store.getUserId().equals(userId)) {
            throw new NotOwnedStoreException();
        }
    }

    public void validateOwnedBusinessLicenseImgKey(Long storeId, String businessLicenseImgFileKey) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(NotExistingStoreException::new);
        if(!store.getBusinessLicenseImgId().equals(businessLicenseImgFileKey)) {
            throw new NotOwnedBusinessLicenseImgIdException();
        }
    }
}
