package io.aksenaksen.demo.usms.warning.service;

import io.aksenaksen.demo.usms.store.domain.Store;
import io.aksenaksen.demo.usms.warning.dto.RegionWarningDto;
import io.aksenaksen.demo.usms.warning.repository.RegionWarning;
import io.aksenaksen.demo.usms.warning.repository.RegionWarningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.aksenaksen.demo.usms.warning.RegionWarningConstant.*;

@Service
@RequiredArgsConstructor
public class RegionWarningService {

    private final RegionWarningRepository regionWarningRepository;

    @Transactional(readOnly = true)
    public List<RegionWarningDto> findByRegion(String region, String startDate, String endDate, int offset, int size) {
        if(startDate == null) {
            startDate = "2000-01-01";
        }
        if(endDate == null) {
            endDate = LocalDate.now().toString();
        }

        return regionWarningRepository.findByRegion(region, startDate, endDate, offset, size)
                .stream()
                .map(RegionWarningDto::new)
                .collect(Collectors.toList());
    }


    @Async("threadPoolTaskExecutor")
    @Transactional
    @Scheduled(cron = "${usms.schedule.createRegionWarning.cron}", zone = "${usms.schedule.timeZone}")
    public void createRegionWarning() {

        long endTimestamp = System.currentTimeMillis();
        long startTimestamp = endTimestamp - 1000*60*60*24;

        int offset = 0;
        int size = 100;

        Map<String, Long> regionBehaviorMap = new HashMap<>();

        while(true) {
            List<AccidentRegionDto> accidentRegionDtos = accidentRepository.findAccidentRegion(startTimestamp, endTimestamp, offset, size);
            for(AccidentRegionDto accidentRegion : accidentRegionDtos) {
                String key = accidentRegion.getBehavior().getCode() + " "
                        + accidentRegion.getStoreAddress().split(" ")[0] + " "
                        + accidentRegion.getStoreAddress().split(" ")[1];

                regionBehaviorMap.put(key, regionBehaviorMap.getOrDefault(key, 0L) + 1);
            }

            if(accidentRegionDtos.size() != size) {
                break;
            }
            offset += size;
        }

        regionBehaviorMap.forEach((key, value) -> {

            if(value > MIN_REGION_WARNING_COUNT) {
                String[] keyChunk = key.split(" ");

                RegionWarning regionWarning = new RegionWarning();
                regionWarning.setBehavior(AccidentBehavior.valueOfCode(Integer.parseInt(keyChunk[0])));
                regionWarning.setRegion(keyChunk[1] + " " + keyChunk[2]);
                regionWarning.setCount(value);
                regionWarning.setDate(Date.valueOf(LocalDate.now().minusDays(1)));

                regionWarningRepository.save(regionWarning);
            }
        });
    }

    @Async("threadPoolTaskExecutor")
    @Scheduled(cron = "${usms.schedule.sendRegionWarningNotification.cron}", zone = "${usms.schedule.timeZone}")
    public void sendRegionWarningNotification() throws IOException {

        int offset = 0;
        int size = 100;

        while (true) {
            List<RegionWarning> regionWarnings = regionWarningRepository
                    .findAll(LocalDate.now().minusDays(1).toString(), LocalDate.now().toString(), offset, size);

            for(RegionWarning regionWarning : regionWarnings) {
                //지역을 통해 매장 찾아냄
                String region = regionWarning.getRegion();
                String regionLikeKeyword = region + "%";
                List<Store> stores = storeRepository.findByRegion(regionLikeKeyword);

                //매장을 소유한 유저에게 알림을 전송
                for(Store store : stores) {
                    Long userId = store.getUserId();
                    List<UsmsDevice> devices = deviceRepository.findByUserId(userId);

                    for(UsmsDevice device : devices) {
                        firebaseNotificationService.send(
                                device.getToken(),
                                REGION_WARNING_SUBJECT,
                                String.format(REGION_WARNING_MESSAGE, region, regionWarning.getBehavior(), regionWarning.getCount())
                        );
                    }
                }
            }

            if(regionWarnings.size() != size) {
                break;
            }
            offset++;
        }

    }
}
