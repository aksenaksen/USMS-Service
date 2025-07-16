package io.aksenaksen.demo.usms.accident.dto;

import io.aksenaksen.demo.usms.accident.repository.AccidentBehavior;
import io.aksenaksen.demo.usms.accident.repository.Accident;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccidentDto {

    private Long id;
    private Long cctvId;
    private AccidentBehavior behavior;
    private long startTimestamp;

    public AccidentDto(Accident accident) {

        this.id = accident.getId();
        this.cctvId = accident.getCctvId();
        this.behavior = accident.getBehavior();
        this.startTimestamp = accident.getStartTimestamp();
    }

}
