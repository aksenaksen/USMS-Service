package io.aksenaksen.demo.usms.video.adaptor.webapi;

import io.aksenaksen.demo.usms.auth.domain.UsmsUserDetails;
import io.aksenaksen.demo.usms.cctv.application.CctvDto;
import io.aksenaksen.demo.usms.cctv.application.provided.CctvServicePort;
import io.aksenaksen.demo.usms.store.application.provided.StoreServicePort;
import io.aksenaksen.demo.usms.store.domain.StoreState;
import io.aksenaksen.demo.usms.store.domain.dto.StoreDto;
import io.aksenaksen.demo.usms.store.domain.exception.UnavailableStoreException;
import io.aksenaksen.demo.usms.video.annotation.LiveVideoFilename;
import io.aksenaksen.demo.usms.video.annotation.StreamKey;
import io.aksenaksen.demo.usms.video.application.provided.VideoServicePort;
import io.aksenaksen.demo.usms.video.exception.ExpiredStreamKeyException;
import io.aksenaksen.demo.usms.video.exception.NotOwnedStreamKeyException;
import io.aksenaksen.demo.usms.video.util.ProtocolAndFileFormatMatcher;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.INVALID_DATE_FORMAT_MESSAGE;
import static io.aksenaksen.demo.usms.video.constant.VideoConstants.DATE_FORMAT_REGEX;


@Validated
@RestController
@RequiredArgsConstructor
public class VideoController {

    private final StoreServicePort storeService;
    private final CctvServicePort cctvService;
    private final VideoServicePort videoService;

    @GetMapping("/video/{protocol}/live/{streamKey}/{filename}")
    public ResponseEntity<Void> getLiveVideo(@PathVariable String protocol,
                                             @PathVariable @StreamKey String streamKey,
                                             @PathVariable @LiveVideoFilename String filename) {

        validateFileFormat(protocol, filename);

        String userId = ((UsmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();

        validateOwnStreamKey(userId, streamKey);
        String redirectUrl = videoService.getLiveVideo(streamKey, protocol, filename);

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @GetMapping("/video/{protocol}/replay/{streamKey}/{filename}")
    public ResponseEntity<byte[]> getReplayVideo(@PathVariable String protocol,
                                                 @PathVariable @StreamKey String streamKey,
                                                 @PathVariable String filename) {
        validateFileFormat(protocol, filename);

        String userId = ((UsmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();

        byte[] videoStream = videoService.getReplayVideo(streamKey, filename);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(videoStream);
    }

    @GetMapping("/api/users/{userId}/stores/{storeId}/cctvs/{cctvId}/replay")
    public List<String> getReplayVideoFilenames(@PathVariable String userId,
                                                @PathVariable Long storeId,
                                                @PathVariable Long cctvId,
                                                @RequestParam
                                                    @NotEmpty(message = INVALID_DATE_FORMAT_MESSAGE)
                                                    @Pattern(regexp = DATE_FORMAT_REGEX, message = INVALID_DATE_FORMAT_MESSAGE)
                                                    String date) {

        userId = ((UsmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();

        storeService.validateOwnedStore(storeId, userId);
        cctvService.validateOwnedCctv(storeId, cctvId);

        if(!storeService.isAvailable(storeId)) {
            throw new UnavailableStoreException();
        }

        String streamKey = cctvService.findById(cctvId).getCctvStreamKey();

        return videoService.getReplayVideoFilenames(cctvId, date, streamKey);
    }

    private void validateFileFormat(String protocol, String filename) {

        String fileFormat = filename.split("[.]")[1];
        ProtocolAndFileFormatMatcher.matches(protocol, fileFormat);
    }

    private void validateOwnStreamKey(String userId, String streamKey) {
        CctvDto cctv = cctvService.findByStreamKey(streamKey);
        if(cctv.isExpired()) {
            throw new ExpiredStreamKeyException();
        }

        StoreDto store = storeService.find(cctv.getStoreId());
        if (!store.getUserId().equals(userId)) {
            throw new NotOwnedStreamKeyException();
        }
        if(store.getStoreState() != StoreState.APPROVAL) {
            throw new UnavailableStoreException();
        }
    }
}
