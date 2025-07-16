package io.aksenaksen.demo.usms.store.adaptor.integration;

import io.aksenaksen.demo.usms.store.domain.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataJpaStoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {

    List<Store> findByUserId(String userId, Pageable Pageable);

    List<Store> findByStoreAddressLike(String region);
}
