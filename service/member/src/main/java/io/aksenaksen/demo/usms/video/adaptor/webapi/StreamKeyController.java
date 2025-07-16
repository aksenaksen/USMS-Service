package io.aksenaksen.demo.usms.video.adaptor.webapi;

import io.aksenaksen.demo.usms.cctv.application.CctvDto;
import io.aksenaksen.demo.usms.cctv.application.provided.CctvServicePort;
import io.aksenaksen.demo.usms.video.application.dto.HttpRequestCheckingStreamDto;
import io.aksenaksen.demo.usms.video.exception.ExpiredStreamKeyException;
import io.aksenaksen.demo.usms.video.exception.NotExistingStreamKeyException;
import io.aksenaksen.demo.usms.video.application.provided.StreamKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StreamKeyController {

    private final StreamKeyService streamKeyService;
    private final CctvServicePort cctvService;

    @PostMapping("/live-streaming/checking")
    public ResponseEntity<Void> checkAuthOfPushingStream(@ModelAttribute HttpRequestCheckingStreamDto requestParam) {

        String streamKey = requestParam.getName();
        // 해당 키 값이 유효한지 체크
        CctvDto cctvDto = cctvService.findByStreamKey(streamKey);
        if(cctvDto == null) {
            throw new NotExistingStreamKeyException();
        }
        if(cctvDto.isExpired()) {
            throw new ExpiredStreamKeyException();
        }

        streamKeyService.checkAuthOfPushingStream(cctvDto.getCctvStreamKey(), requestParam.getTime());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
