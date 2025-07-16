package io.aksenaksen.demo.usms.video.application.provided;


import io.aksenaksen.demo.usms.video.exception.AlreadyConnectedStreamKeyException;
import io.aksenaksen.demo.usms.video.application.required.StreamKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StreamKeyService {

    private final StreamKeyRepository streamKeyRepository;

    @Value("${usms.stream-key.ttl}")
    private int streamKeyExpireTimeSecond;

    @Transactional
    public void checkAuthOfPushingStream(String streamKey, long connectedTime) {

        // 이미 기존에 연결된 상태일 경우 키 만료 시간 갱신 후 리턴
        if(connectedTime != 0L) {
            streamKeyRepository.updateExpireTime(streamKey, streamKeyExpireTimeSecond);
            return;
        }
        // 초기 연결 시 해당 키 값이 이미 연결된 상태인지 체크 후 키 등록
        if(streamKeyRepository.isExistingStreamKey(streamKey)) {
            throw new AlreadyConnectedStreamKeyException();
        }
        streamKeyRepository.saveStreamKey(streamKey, streamKeyExpireTimeSecond);
    }
}
