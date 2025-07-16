package io.aksenaksen.demo.usms.cctv.application;

import io.aksenaksen.demo.usms.cctv.domain.Cctv;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CctvDto {

    private long id;
    private long storeId;
    private String cctvName;
    private String cctvStreamKey;
    private boolean isExpired;
    private Boolean isConnected;

    public static CctvDto from(Cctv cctv) {
        CctvDto dto = new CctvDto();
        dto.id = cctv.getId();
        dto.storeId = cctv.getStoreId();
        dto.cctvName = cctv.getName();
        dto.cctvStreamKey = cctv.getStreamKey();
        dto.isExpired = cctv.isExpired();
        dto.isConnected= cctv.isConnect();
        return dto;
    }

}
