package io.aksenaksen.demo.usms.cctv.application.provided;

import io.aksenaksen.demo.usms.cctv.application.CctvDto;

import java.util.List;

public interface CctvServicePort {

    CctvDto createCctv(Long storeId, String name);

    void changeCctvName(Long cctvId, String cctvName);

    void deleteCctv(Long cctvId);

    void validateOwnedCctv(Long storeId, Long cctvId);

    CctvDto findById(Long id);

    CctvDto findByStreamKey(String streamKey);

    List<CctvDto> findAllByStoreId(long storeId, int offset, int size);

    void validateStoreAccess(Long storeId, String userId);
}
