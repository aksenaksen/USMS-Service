package io.aksenaksen.demo.usms.cctv.application.provided;



import io.aksenaksen.demo.usms.cctv.domain.Cctv;
import io.aksenaksen.demo.usms.cctv.application.CctvDto;
import io.aksenaksen.demo.usms.cctv.exception.NotOwnedCctvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CctvService {

    private final CctvCommandService cctvCommandService;
    private final CctvQueryService cctvQueryService;

    @Transactional
    public CctvDto createCctv(Long storeId, String name) {

        return CctvDto.from(cctvCommandService.createCctv(storeId,name));
    }


    @Transactional
    public void changeCctvName(Long cctvId, String cctvName) {

        cctvCommandService.modify(cctvId,cctvName);
    }

    @Transactional
    public void deleteCctv(Long cctvId) {

        cctvCommandService.delete(cctvId);
    }

    @Transactional
    public void validateOwnedCctv(Long storeId, Long cctvId) {
        Cctv cctv = cctvQueryService.findById(cctvId);
//        if(streamKeyService.isExistingStreamKey(cctv.getStreamKey())) cctvCommandService.activateCctv(cctv.getId());
        if(cctv.getStoreId() != storeId) {
            throw new NotOwnedCctvException();
        }
    }
//        cctv.activateConnect(streamKeyRepository.isExistingStreamKey(cctv.getStreamKey()));

    @Transactional
    public CctvDto findById(Long id) {

        Cctv cctv = cctvQueryService.findById(id);
//        if(streamKeyService.isExistingStreamKey(cctv.getStreamKey())) cctvCommandService.activateCctv(cctv.getId());

        return CctvDto.from(cctv);
    }

    @Transactional
    public CctvDto findByStreamKey(String streamKey) {

        Cctv cctv = cctvQueryService.findByStreamKey(streamKey);
//        if(streamKeyService.isExistingStreamKey(cctv.getStreamKey())) cctvCommandService.activateCctv(cctv.getId());

        return CctvDto.from(cctv);
    }

    @Transactional(readOnly = true)
    public List<CctvDto> findAllByStoreId(long storeId, int offset, int size) {
        return cctvQueryService.findAllByStoreId(storeId,offset,size)
                .stream()
                .map(CctvDto::from)
                .toList();
    }

}
