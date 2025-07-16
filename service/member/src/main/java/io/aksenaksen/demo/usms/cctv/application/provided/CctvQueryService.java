package io.aksenaksen.demo.usms.cctv.application.provided;

import io.aksenaksen.demo.usms.cctv.domain.Cctv;
import io.aksenaksen.demo.usms.cctv.application.required.CctvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CctvQueryService {

    private final CctvRepository cctvRepository;

    public Cctv findById(Long id) {

        return cctvRepository.findById(id);
    }

    public Cctv findByStreamKey(String streamKey) {
        return cctvRepository.findByStreamKey(streamKey);
    }

    public List<Cctv> findAllByStoreId(long storeId, int offset, int size) {
        return cctvRepository.findByStoreId(storeId, offset, size);
    }
}
