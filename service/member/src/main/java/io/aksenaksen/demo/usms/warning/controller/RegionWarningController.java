package io.aksenaksen.demo.usms.warning.controller;

import io.aksenaksen.demo.usms.auth.domain.UsmsUserDetails;
import io.aksenaksen.demo.usms.store.application.provided.StoreServicePort;
import io.aksenaksen.demo.usms.store.domain.exception.UnavailableStoreException;
import io.aksenaksen.demo.usms.warning.dto.HttpRequestRetrievingRegionWarningDto;
import io.aksenaksen.demo.usms.warning.dto.RegionWarningDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegionWarningController {

    private final StoreServicePort storeService;
    private final RegionWarningService regionWarningService;

    @GetMapping("/api/users/{userId}/stores/{storeId}/accidents/region")
    public List<RegionWarningDto> findAllByStoreId(@PathVariable String userId,
                                                   @PathVariable Long storeId,
                                                   @ModelAttribute @Valid HttpRequestRetrievingRegionWarningDto requestParams) {

        validateDateFlow(requestParams.getStartDate(), requestParams.getEndDate());

        userId = ((UsmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember().getId();
        storeService.validateOwnedStore(storeId, userId);

        if(!storeService.isAvailable(storeId)) {
            throw new UnavailableStoreException();
        }


        return regionWarningService.findByRegion(storeService.getRegion(storeId),
                                                requestParams.getStartDate(),
                                                requestParams.getEndDate(),
                                                requestParams.getOffset(),
                                                requestParams.getSize());
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
