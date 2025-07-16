package io.aksenaksen.demo.usms.accident.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cctv_id")
    private Long cctvId;

    @Column(name = "behavior")
    @Convert(converter = AccidentBehaviorConverter.class)
    private AccidentBehavior behavior;

    @Column(name = "start_timestamp")
    private Long startTimestamp;

    public static Accident init(Long cctvId, AccidentBehavior behavior, Long timestamp) {

        Accident accident = new Accident();
        accident.setCctvId(cctvId);
        accident.setBehavior(behavior);
        accident.setStartTimestamp(timestamp);

        return accident;
    }
}
