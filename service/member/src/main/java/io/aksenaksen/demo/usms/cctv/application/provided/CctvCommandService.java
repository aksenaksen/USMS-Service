package io.aksenaksen.demo.usms.cctv.application.provided;

import io.aksenaksen.demo.usms.cctv.domain.Cctv;
import io.aksenaksen.demo.usms.cctv.application.required.CctvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CctvCommandService {

    private final CctvRepository cctvRepository;

    @Transactional
    public Cctv createCctv(Long storeId, String name) {

        Cctv cctv = Cctv.create(storeId, name);

        cctvRepository.save(cctv);

        return cctv;
    }

    public void modify(Long cctvId, String cctvName) {

        Cctv cctv = cctvRepository.findById(cctvId);
        cctv.changeCctvName(cctvName);

        cctvRepository.update(cctv);
    }

    public void delete(Long cctvId) {
        Cctv cctv = cctvRepository.findById(cctvId);
        cctvRepository.delete(cctv);
    }

    public void activateCctv(Long cctvId){
        Cctv cctv = cctvRepository.findById(cctvId);
        cctv.activateConnect();
        cctvRepository.update(cctv);
    }
}
