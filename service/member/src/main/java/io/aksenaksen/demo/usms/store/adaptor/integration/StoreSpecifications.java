package io.aksenaksen.demo.usms.store.adaptor.integration;

import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.store.domain.StoreState;
import org.springframework.data.jpa.domain.Specification;

public class StoreSpecifications {

    static Specification<Store> hasUserId(String userId) {

        return (store, cq, cb) -> cb.equal(store.get("userId"), userId);
    }

    static Specification<Store> hasBusinessLicenseCode(String businessLicenseCode) {

        return (store, cq, cb) -> cb.equal(store.get("businessLicenseCode"), businessLicenseCode);
    }

    static Specification<Store> hasUserState(StoreState state) {

        return (store, cq, cb) -> cb.equal(store.get("storeState"), state.getCode());
    }
}
