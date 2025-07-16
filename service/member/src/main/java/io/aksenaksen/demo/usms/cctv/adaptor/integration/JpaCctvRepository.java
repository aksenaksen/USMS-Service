package io.aksenaksen.demo.usms.cctv.adaptor.integration;

import io.aksenaksen.demo.usms.cctv.exception.NotExistingCctvException;
import io.aksenaksen.demo.usms.video.exception.NotExistingStreamKeyException;
import io.aksenaksen.demo.usms.cctv.application.required.CctvRepository;
import io.aksenaksen.demo.usms.cctv.domain.Cctv;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaCctvRepository implements CctvRepository {

    private final SpringDataJpaCctvRepository springDataJpaCctvRepository;

    @Override
    public Cctv save(Cctv cctv) {

        return springDataJpaCctvRepository.save(cctv);
    }

    @Override
    public Cctv findById(Long id) {

        return springDataJpaCctvRepository.findById(id)
                .orElseThrow(NotExistingCctvException::new);
    }

    @Override
    public List<Cctv> findByStoreId(Long storeId, int offset, int size) {

        return springDataJpaCctvRepository.findByStoreId(storeId, PageRequest.of(offset, size));
    }

    @Override
    public Cctv findByStreamKey(String streamKey) {

        return springDataJpaCctvRepository.findByStreamKey(streamKey)
                .orElseThrow(NotExistingStreamKeyException::new);
    }

    @Override
    public void update(Cctv cctv) {

    }

    @Override
    public void delete(Cctv cctv) {

        springDataJpaCctvRepository.delete(cctv);
    }
}
