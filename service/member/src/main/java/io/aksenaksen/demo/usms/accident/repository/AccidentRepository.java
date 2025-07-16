package io.aksenaksen.demo.usms.accident.repository;

import io.aksenaksen.demo.usms.accident.dto.AccidentRegionDto;
import io.aksenaksen.demo.usms.accident.dto.AccidentStatDto;

import java.util.List;

public interface AccidentRepository {

    void save(Accident accident);

    List<Accident> findAllByCctvId(Long cctvId, int offset, int size);

    List<AccidentStatDto> findAccidentStats(long storeId, long startTimestamp, long endTimestamp);

    List<Accident> findAllByStoreId(long storeId, long startTimestamp, long endTimestamp, int offset, int size);

    List<Accident> findAllByStoreId(long storeId, List<AccidentBehavior> behavior, long startTimestamp, long endTimestamp, int offset, int size);

    List<AccidentRegionDto> findAccidentRegion(long startTimestamp, long endTimestamp, int offset, int size);
}
