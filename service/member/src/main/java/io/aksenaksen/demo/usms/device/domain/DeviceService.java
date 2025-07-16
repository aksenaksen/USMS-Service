package io.aksenaksen.demo.usms.device.domain;

import com.ssg.usms.business.device.repository.DeviceRepository;
import com.ssg.usms.business.device.repository.UsmsDevice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Transactional
    public void saveToken(String token,Long userid){

        if(!deviceRepository.existsByToken(token)) {
            deviceRepository.saveToken(UsmsDevice.builder()
                    .userId(userid)
                    .token(token)
                    .build());
        }
    }
    @Transactional
    public void deleteToken(Long userid){

        if(deviceRepository.deleteToken(userid)==0){
            throw new RuntimeException();
        }
    }
}
