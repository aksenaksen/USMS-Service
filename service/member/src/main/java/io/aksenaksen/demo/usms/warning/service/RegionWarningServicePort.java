package io.aksenaksen.demo.usms.warning.service;

import io.aksenaksen.demo.usms.warning.dto.RegionWarningDto;

import java.util.List;

public interface RegionWarningServicePort {
    List<RegionWarningDto> findByRegion(String region, String startDate, String endDate, int offset, int size);
}
