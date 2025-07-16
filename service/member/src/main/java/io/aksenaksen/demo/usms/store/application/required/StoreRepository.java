package io.aksenaksen.demo.usms.store.application.required;

import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.store.domain.StoreState;

import java.util.List;
import java.util.Optional;

public interface StoreRepository {

    Store save(Store store);

    Optional<Store> findById(Long id);

    List<Store> findAll(String userId, String businessCode, StoreState state, int offset, int size);

    List<Store> findByUserId(String userId, int offset, int size);

    List<Store> findByRegion(String region);

    void update(Store store);

    void delete(Store store);

}
