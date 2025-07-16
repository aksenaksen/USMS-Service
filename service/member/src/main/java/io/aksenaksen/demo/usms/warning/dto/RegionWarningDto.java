package io.aksenaksen.demo.usms.warning.dto;

import io.aksenaksen.demo.usms.warning.repository.RegionWarning;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class RegionWarningDto {

    private Long id;
    private String region;
    private AccidentBehavior behavior;
    private long count;
    private LocalDate date;

    public RegionWarningDto(RegionWarning regionWarning) {

        this.id = regionWarning.getId();
        this.region = regionWarning.getRegion();
        this.behavior = regionWarning.getBehavior();
        this.count = regionWarning.getCount();
        this.date = regionWarning.getDate().toLocalDate();
    }
}
