package io.aksenaksen.demo.usms.cctv.adaptor.webapi;


import io.aksenaksen.demo.usms.auth.domain.UsmsUserDetails;
import io.aksenaksen.demo.usms.cctv.application.provided.CctvServicePort;
import io.aksenaksen.demo.usms.cctv.application.CctvDto;
import io.aksenaksen.demo.usms.cctv.domain.dto.HttpRequestCreatingCctvDto;
import io.aksenaksen.demo.usms.cctv.domain.dto.HttpRequestUpdatingCctvDto;
import io.aksenaksen.demo.usms.store.application.provided.StoreServicePort;
import io.aksenaksen.demo.usms.store.domain.exception.UnavailableStoreException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_ALLOWED_PAGE_OFFSET_FORMAT_MESSAGE;
import static io.aksenaksen.demo.usms.constant.CustomStatusCode.NOT_ALLOWED_PAGE_SIZE_FORMAT_MESSAGE;


@Validated
@RestController
@RequiredArgsConstructor
public class CctvWebApi {

    private final CctvServicePort cctvService;
    private final StoreServicePort storeService;

    @PostMapping("api/users/{userId}/stores/{storeId}/cctvs")
    public CctvDto createCctv(@PathVariable String userId,
                              @PathVariable Long storeId,
                              @RequestBody @Valid HttpRequestCreatingCctvDto requestBody) {

        // 검증
        userId = getMemberId(userId);
        storeService.validateOwnedStore(storeId,userId);
        if(!storeService.isAvailable(storeId)) {
            throw new UnavailableStoreException();
        }
        // 비지니스 로직
        return cctvService.createCctv(storeId, requestBody.getName());
    }

    @GetMapping("api/users/{userId}/stores/{storeId}/cctvs/{cctvId}")
    public CctvDto findById(@PathVariable String userId,
                            @PathVariable Long storeId,
                            @PathVariable Long cctvId) {

        userId = getMemberId(userId);
        storeService.validateOwnedStore(storeId, userId);
        cctvService.validateOwnedCctv(storeId, cctvId);
        if(!storeService.isAvailable(storeId)) {
            throw new UnavailableStoreException();
        }

        // 비지니스 로직
        return cctvService.findById(cctvId);
    }

    @GetMapping("api/users/{userId}/stores/{storeId}/cctvs")
    public List<CctvDto> findAllByStoreId(@PathVariable String userId,
                                          @PathVariable Long storeId,
                                          @RequestParam(required = false)
                                              @NotNull(message = NOT_ALLOWED_PAGE_OFFSET_FORMAT_MESSAGE)
                                              @PositiveOrZero(message = NOT_ALLOWED_PAGE_OFFSET_FORMAT_MESSAGE)
                                              Integer offset,
                                          @RequestParam(required = false)
                                              @NotNull(message = NOT_ALLOWED_PAGE_SIZE_FORMAT_MESSAGE)
                                              @Positive(message = NOT_ALLOWED_PAGE_SIZE_FORMAT_MESSAGE)
                                              Integer size) {

        userId = getMemberId(userId);
        storeService.validateOwnedStore(storeId, userId);
        if(!storeService.isAvailable(storeId)) {
            throw new UnavailableStoreException();
        }

        // 비지니스 로직
        return cctvService.findAllByStoreId(storeId, offset, size);
    }

    @PatchMapping("api/users/{userId}/stores/{storeId}/cctvs/{cctvId}")
    public ResponseEntity<Void> update(@PathVariable String userId,
                                       @PathVariable Long storeId,
                                       @PathVariable Long cctvId,
                                       @RequestBody @Valid HttpRequestUpdatingCctvDto requestBody) {

        // 본인 소유의 cctv가 맞는지 검증
        userId = getMemberId(userId);
        storeService.validateOwnedStore(storeId, userId);
        cctvService.validateOwnedCctv(storeId, cctvId);
        if(!storeService.isAvailable(storeId)) {
            throw new UnavailableStoreException();
        }

        // 비지니스 로직
        cctvService.changeCctvName(cctvId, requestBody.getName());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("api/users/{userId}/stores/{storeId}/cctvs/{cctvId}")
    public ResponseEntity<Void> delete(@PathVariable String userId,
                                       @PathVariable Long storeId,
                                       @PathVariable Long cctvId) {

        // 본인 소유의 cctv가 맞는지 검증
        userId = getMemberId(userId);
        storeService.validateOwnedStore(storeId, userId);
        cctvService.validateOwnedCctv(storeId, cctvId);
        if(!storeService.isAvailable(storeId)) {
            throw new UnavailableStoreException();
        }

        // 비지니스 로직
        cctvService.deleteCctv(cctvId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    private String getMemberId(String userId) {
        userId = ((UsmsUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();
        return userId;
    }
}
