package io.aksenaksen.demo.usms.cctv.application.required;

import io.aksenaksen.demo.usms.cctv.domain.Cctv;

import java.util.List;

public interface CctvRepository {

    Cctv save(Cctv cctv);

    Cctv findById(Long id);

    List<Cctv> findByStoreId(Long storeId, int offset, int size);

    Cctv findByStreamKey(String streamKey);

    void update(Cctv cctv);

    void delete(Cctv cctv);
}
