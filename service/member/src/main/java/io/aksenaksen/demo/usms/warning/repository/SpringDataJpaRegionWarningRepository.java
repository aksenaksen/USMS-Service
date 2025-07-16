package io.aksenaksen.demo.usms.warning.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface SpringDataJpaRegionWarningRepository extends JpaRepository<RegionWarning, Long> {


    List<RegionWarning> findByRegionAndDateBetween(String region, Date startDate, Date endDate, Pageable pageable);

    List<RegionWarning> findByDateBetween(Date startDate, Date endDate, PageRequest of);
}
