package io.aksenaksen.demo.usms.accident.controller;

import io.aksenaksen.demo.usms.accident.dto.*;
import io.aksenaksen.demo.usms.accident.exception.InvalidDateFlowException;
import io.aksenaksen.demo.usms.accident.service.AccidentService;
import io.aksenaksen.demo.usms.auth.domain.UsmsUserDetails;
import io.aksenaksen.demo.usms.cctv.application.provided.CctvServicePort;
import io.aksenaksen.demo.usms.store.application.provided.StoreServicePort;
import io.aksenaksen.demo.usms.store.domain.dto.StoreDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccidentController {

    private final StoreServicePort storeService;
    private final CctvServicePort cctvService;
    private final AccidentService accidentService;

    @PostMapping("/live-streaming/accidents")
    public ResponseEntity<Void> createAccident(@RequestBody @Valid HttpRequestCreatingAccidentDto requestBody) {

        cctvService.findByStreamKey(requestBody.getStreamKey());
        StoreDto storeDto = storeService.find(requestBody.get)
        accidentService.createAccident(requestBody.getStreamKey(),
                requestBody.getBehavior(),
                requestBody.getStartTimestamp()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/users/{userId}/stores/{storeId}/cctvs/accidents")
    public List<AccidentDto> findAllByStoreId(@PathVariable String userId,
                                              @PathVariable Long storeId,
                                              @ModelAttribute @Valid HttpRequestRetrievingAccidentDto requestParam) {

        validateDateFlow(requestParam.getStartDate(), requestParam.getEndDate());

        userId = ((UsmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();
        storeService.validateOwnedStore(storeId, userId);

        return accidentService.findByStoreId(storeId, requestParam);
    }

    @GetMapping("/api/users/{userId}/stores/{storeId}/cctvs/accidents/stats")
    public List<AccidentStatDto> findAccidentStatsByStoreId(@PathVariable String userId,
                                                            @PathVariable Long storeId,
                                                            @ModelAttribute @Valid HttpRequestRetrievingAccidentStatDto requestParam) {

        validateDateFlow(requestParam.getStartDate(), requestParam.getEndDate());

        userId = ((UsmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();
        storeService.validateOwnedStore(storeId, userId);

        return accidentService.findAccidentStatsByStoreId(storeId, requestParam.getStartDate(), requestParam.getEndDate());
    }

    private void validateDateFlow(String startDate, String endDate) {

        if(startDate == null || endDate == null) {
            return;
        }
        if(LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
            throw new InvalidDateFlowException();
        }
    }
}
