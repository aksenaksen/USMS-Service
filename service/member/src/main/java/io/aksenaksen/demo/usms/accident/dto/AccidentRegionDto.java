package io.aksenaksen.demo.usms.accident.dto;

import io.aksenaksen.demo.usms.accident.repository.AccidentBehavior;
import lombok.*;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccidentRegionDto {

    private Long accidentId;
    private String storeAddress;
    private AccidentBehavior behavior;
}
