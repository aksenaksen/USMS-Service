package io.aksenaksen.demo.usms.device.adaptor.persistence;

import io.aksenaksen.demo.usms.device.domain.UsmsDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringJpaDataDeviceRepository extends JpaRepository<UsmsDevice,Long> {

    List<UsmsDevice> findByUserId(Long userId);

    int deleteByUserId(Long userid);

    boolean existsByToken(String token);
}
