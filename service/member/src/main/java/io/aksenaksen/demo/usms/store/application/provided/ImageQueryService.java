package io.aksenaksen.demo.usms.store.application.provided;

import io.aksenaksen.demo.usms.store.domain.dto.ImageDto;
import io.aksenaksen.demo.usms.store.domain.exception.NotExistingBusinessLicenseImgFileKeyException;
import io.aksenaksen.demo.usms.store.application.required.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageQueryService {

    private final ImageRepository imageRepository;

    public ImageDto findBusinessLicenseImgFile(String businessLicenseImgFileKey) {

        if(!imageRepository.isExisting(businessLicenseImgFileKey)) {
            throw new NotExistingBusinessLicenseImgFileKeyException();
        }
        return imageRepository.find(businessLicenseImgFileKey);
    }
}
