package io.aksenaksen.demo.usms.video.application.provided;

import io.aksenaksen.demo.usms.video.application.required.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Setter
@Service
@RequiredArgsConstructor
public class VideoService implements VideoServicePort {

    private final VideoRepository videoRepository;

    @Value("${usms.media-server.url}")
    private String mediaServerUrl;

    @Transactional(readOnly = true)
    public String getLiveVideo(String streamKey, String protocol, String filename) {

        // 해당 파일에 대한 URL 리다이렉트
        return String.format("%s/video/%s/live/%s/%s", mediaServerUrl, protocol, streamKey, filename);
    }

    @Transactional(readOnly = true)
    public byte[] getReplayVideo(String streamKey, String filename) {

//        validateOwnStreamKey(userId, streamKey);

        // filename : streamKey(UUID 형태)-1641900000000.m3u8 or streamKey(UUID 형태)-1641900000000-001.ts
        long timestamp = Long.parseLong(filename.split("[.]")[0].split("-")[5]);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());

        // 실제 다시보기 파일 경로 : /streamKey/년/월/일/파일명
        String replayVideoRealPath = Paths.get(
                                            streamKey,
                                            Integer.toString(dateTime.getYear()),
                                            Integer.toString(dateTime.getMonth().getValue()),
                                            Integer.toString(dateTime.getDayOfMonth()),
                                            filename
                                    ).toString();

        return videoRepository.getVideo(replayVideoRealPath);
    }

    @Transactional
    public List<String> getReplayVideoFilenames(Long cctvId, String date, String streamKey) {

        LocalDate localDate = LocalDate.parse(date);
        String path = Paths.get(streamKey,
                                Integer.toString(localDate.getYear()),
                                Integer.toString(localDate.getMonth().getValue()),
                                Integer.toString(localDate.getDayOfMonth()))
                        .toString();

        return videoRepository.getVideoFilenames(path);
    }


}
