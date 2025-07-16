package io.aksenaksen.demo.usms.store.adaptor.webapi;


import io.aksenaksen.demo.usms.auth.domain.UsmsUserDetails;
import io.aksenaksen.demo.usms.member.domain.MemberRole;
import io.aksenaksen.demo.usms.store.annotation.BusinessLicenseImgKey;
import io.aksenaksen.demo.usms.store.application.provided.StoreServicePort;
import io.aksenaksen.demo.usms.store.domain.dto.*;
import io.aksenaksen.demo.usms.store.domain.exception.EmptyImgFileException;
import io.aksenaksen.demo.usms.store.domain.exception.NotAllowedImgFileFormatException;
import io.aksenaksen.demo.usms.store.application.provided.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


import static io.aksenaksen.demo.usms.store.constant.StoreConstants.ALLOWED_IMG_FILE_FORMATS;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreServicePort storeService;

    @PostMapping("/api/users/{userId}/stores")
    public ResponseEntity<StoreDto> createStore(@PathVariable String userId,
                                                @ModelAttribute @Valid HttpRequestCreatingStoreDto requestParam,
                                                @RequestPart(name = "businessLicenseImg") MultipartFile businessLicenseImgFile)
            throws IOException {

        userId = getMemberId(userId);
        //multipartFile 포맷 확인
        if(businessLicenseImgFile.isEmpty()) {
            throw new EmptyImgFileException();
        }
        String fileFormat = businessLicenseImgFile.getOriginalFilename().split("[.]")[1];
        if(!ALLOWED_IMG_FILE_FORMATS.contains(fileFormat)) {
            throw new NotAllowedImgFileFormatException();
        }

        StoreDto store = StoreDto.of(userId,requestParam);

        StoreDto storeDto = storeService.createStore(store, businessLicenseImgFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(storeDto);
    }

    @GetMapping("/api/users/{userId}/stores")
    public List<StoreDto> findStores(@PathVariable String userId,
                                     @ModelAttribute @Valid HttpRequestRetrievingStoreDto requestParam)
            throws IllegalAccessException {

        userId = getMemberId(userId);

        // 현재 세션의 권한에 따라 아래 실행할 서비스 달라짐 ( 1. 관리자일 경우, 2. 점주 고객일 경우 )
        List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();

        if (authorities.size() != 1) {
            throw new IllegalStateException("유저 권한 갯수가 현재 이상함");
        }

        if (authorities.get(0).getAuthority().equals(MemberRole.ADMIN.name())) {
            return storeService.findAll(requestParam.getUser(),
                    requestParam.getBusinessLicenseCode(),
                    requestParam.getStoreState(),
                    requestParam.getOffset(),
                    requestParam.getSize());
        }
        if (authorities.get(0).getAuthority().equals(MemberRole.OWNER.name())) {
            return storeService.findAll(userId, requestParam.getOffset(), requestParam.getSize());
        }
        throw new IllegalAccessException("접근 권한이 없는 유저입니다.");
    }

    @GetMapping("/api/users/{userId}/stores/{storeId}")
    public StoreDto findStoreById(@PathVariable String userId,
                                  @PathVariable Long storeId) {

        userId = getMemberId(userId);

        storeService.validateOwnedStore(storeId, userId);

        return storeService.find(storeId);
    }



    @GetMapping("/api/users/{userId}/stores/{storeId}/license/{licenseKey}")
    public ResponseEntity<byte[]> findBusinessLicenseImgFile(@PathVariable String userId,
                                             @PathVariable Long storeId,
                                             @PathVariable @BusinessLicenseImgKey String licenseKey) {

        userId = getMemberId(userId);

        List<? extends GrantedAuthority> authorities = (List<? extends GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();

        if (authorities.size() != 1) {
            throw new IllegalStateException("유저 권한 갯수가 현재 이상함");
        }
        if (authorities.get(0).getAuthority().equals(MemberRole.OWNER.name())) {
            storeService.validateOwnedStore(storeId, userId);
        }
        storeService.validateOwnedBusinessLicenseImgKey(storeId, licenseKey);

        ImageDto img = storeService.findBusinessLicenseImgFile(licenseKey);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(img.getContentLength())
                .body(img.getContent());
    }

    @PostMapping("/api/users/{userId}/stores/{storeId}")
    public ResponseEntity<Void> updateStore(@PathVariable String userId,
                                            @PathVariable Long storeId,
                                            @ModelAttribute @Valid HttpRequestCreatingStoreDto requestBody,
                                            @RequestPart(name = "businessLicenseImg") MultipartFile businessLicenseImgFile) throws IOException {

        if(businessLicenseImgFile.isEmpty()) {
            throw new EmptyImgFileException();
        }
        String fileFormat = businessLicenseImgFile.getOriginalFilename().split("[.]")[1];
        if(!ALLOWED_IMG_FILE_FORMATS.contains(fileFormat)) {
            throw new NotAllowedImgFileFormatException();
        }
        userId = getMemberId(userId);
        storeService.validateOwnedStore(storeId, userId);

        storeService.update(storeId,
                requestBody.getStoreName(),
                requestBody.getStoreAddress(),
                requestBody.getBusinessLicenseCode(),
                businessLicenseImgFile);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/api/users/{userId}/stores/{storeId}")
    public ResponseEntity<Void> changeStoreState(@PathVariable Long userId,
                                                 @PathVariable Long storeId,
                                                 @RequestBody HttpRequestChangingStoreStateDto requestBody) {

        storeService.changeStoreState(storeId, requestBody.getState(), requestBody.getMessage());

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping("/api/users/{userId}/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable String userId,
                                            @PathVariable Long storeId) {

        userId = getMemberId(userId);

        storeService.validateOwnedStore(storeId, userId);

        storeService.delete(storeId);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    private String getMemberId(String userId) {
        userId = ((UsmsUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();
        return userId;
    }
}
