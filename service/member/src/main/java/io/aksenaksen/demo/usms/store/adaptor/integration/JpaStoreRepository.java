package io.aksenaksen.demo.usms.store.adaptor.integration;

import io.aksenaksen.demo.usms.store.application.required.StoreRepository;
import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.store.domain.StoreState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.aksenaksen.demo.usms.store.adaptor.integration.StoreSpecifications.*;


@Repository
@RequiredArgsConstructor
public class JpaStoreRepository implements StoreRepository {

    private final SpringDataJpaStoreRepository springDataJpaStoreRepository;

    @Override
    public Store save(Store store) {
        return springDataJpaStoreRepository.save(store);
    }

    @Override
    public Optional<Store> findById(Long id) {
        return springDataJpaStoreRepository.findById(id);
    }

    @Override
    public List<Store> findAll(String userId, String businessCode, StoreState state, int offset, int size) {

        Specification<Store> specification = Specification.where(null);
        if(userId != null) {
            specification = specification.and(hasUserId(userId));
        }
        if(businessCode != null) {
            specification = specification.and(hasBusinessLicenseCode(businessCode));
        }
        if(state != null) {
            specification = specification.and(hasUserState(state));
        }
        return springDataJpaStoreRepository.findAll(specification, PageRequest.of(offset, size)).getContent();
    }

    @Override
    public List<Store> findByUserId(String userId, int offset, int size) {

        return springDataJpaStoreRepository.findByUserId(userId, PageRequest.of(offset, size));
    }

    @Override
    public List<Store> findByRegion(String region) {

        return springDataJpaStoreRepository.findByStoreAddressLike(region);
    }

    @Override
    public void update(Store store) {

    }

    @Override
    public void delete(Store store) {
        springDataJpaStoreRepository.delete(store);
    }
}
