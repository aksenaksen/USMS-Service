package io.aksenaksen.demo.usms.cctv.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(indexes = @Index(name = "usms_cctv_stream_key_idx", unique = true, columnList = "cctv_stream_key"))
public class Cctv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "cctv_name")
    private String name;

    @Column(name = "cctv_stream_key")
    private String streamKey;

    @Column(name = "is_expired")
    private boolean isExpired;

    @Column(name = "is_connected")
    private boolean isConnect;

    public static Cctv create(Long storeId, String cctvName) {

        Cctv cctv = new Cctv();
        cctv.storeId = storeId;
        cctv.name = cctvName;
        cctv.streamKey = UUID.randomUUID().toString();
        cctv.isExpired = false;
        cctv.isConnect = false;

        return cctv;
    }

    public void activateConnect(){
        this.isConnect = true;
    }

    public void changeCctvName(String name) {
        this.name = name;
    }

    public void expire() {
        isExpired = true;
    }

    public void makeAvailable() {
        isExpired = false;
    }
}
