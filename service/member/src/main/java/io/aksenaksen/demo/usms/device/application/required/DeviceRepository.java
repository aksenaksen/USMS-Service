package io.aksenaksen.demo.usms.device.application.required;


import io.aksenaksen.demo.usms.device.domain.UsmsDevice;

import java.util.List;

public interface DeviceRepository {

    List<UsmsDevice> findByUserId(Long userId);

    void saveToken(UsmsDevice device);

    boolean existsByToken(String token);

    int deleteToken(Long userid);
}
