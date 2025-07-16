package io.aksenaksen.demo.usms.accident.service;

import io.aksenaksen.demo.usms.accident.repository.Accident;
import io.aksenaksen.demo.usms.accident.repository.AccidentBehavior;
import io.aksenaksen.demo.usms.accident.repository.AccidentRepository;
import io.aksenaksen.demo.usms.cctv.domain.Cctv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccidentService {

    private final AccidentRepository accidentRepository;

    @Transactional
    public void createAccident(String streamKey, AccidentBehavior behavior, Long timestamp) {

        Cctv cctv = cctvRepository.findByStreamKey(streamKey);
        Long cctvId = cctv.getId();

        Store store = storeRepository.findById(cctv.getStoreId());
        Long userId = store.getUserId();

//        List<UsmsDevice> devices = deviceRepository.findByUserId(userId);
//        devices.forEach(device -> {
//            try {
//                firebaseNotificationService.send(
//                        device.getToken(),
//                        ACCIDENT_OCCURRENCE_SUBJECT,
//                        String.format(ACCIDENT_OCCURRENCE_MESSAGE, store.getStoreName(), behavior.name())
//                );
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }); 밑에 이벤트로 처리하는게 낫지않을까

        Accident accident = Accident.init(cctvId, behavior, timestamp);
        accidentRepository.save(accident);
    }

    @Transactional(readOnly = true)
    public List<AccidentDto> findByStoreId(Long storeId, HttpRequestRetrievingAccidentDto requestParam) {

        long startTimestamp = requestParam.getStartDate() == null ? 0L : parseStartTimestamp(requestParam.getStartDate());
        long endTimestamp = requestParam.getEndDate() == null ? System.currentTimeMillis() : parseEndTimestamp(requestParam.getEndDate());

        // 이상 행동 조건이 없을 때
        if(requestParam.getBehavior() == null || requestParam.getBehavior().isEmpty()) {
            return accidentRepository.findAllByStoreId(storeId,
                                                    startTimestamp,
                                                    endTimestamp,
                                                    requestParam.getOffset(),
                                                    requestParam.getSize()
                    )
                    .stream()
                    .map(AccidentDto::new)
                    .collect(Collectors.toList());
        }

        // 이상 행동에 따른 필터링 있을 때
        return accidentRepository.findAllByStoreId(storeId,
                                                requestParam.getBehavior(),
                                                startTimestamp,
                                                endTimestamp,
                                                requestParam.getOffset(),
                                                requestParam.getSize()
                )
                .stream()
                .map(AccidentDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<AccidentStatDto> findAccidentStatsByStoreId(Long storeId, String startDate, String endDate) {

        long startTimestamp = startDate == null ? 0L : parseStartTimestamp(startDate);
        long endTimestamp = endDate == null ? System.currentTimeMillis() : parseEndTimestamp(endDate);

        return accidentRepository.findAccidentStats(storeId, startTimestamp, endTimestamp);
    }

    private long parseStartTimestamp(String startDate) {

        return LocalDate.parse(startDate).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private long parseEndTimestamp(String endDate) {

        return LocalDate.parse(endDate).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
