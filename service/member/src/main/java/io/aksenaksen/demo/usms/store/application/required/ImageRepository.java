package io.aksenaksen.demo.usms.store.application.required;

import io.aksenaksen.demo.usms.store.domain.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {

    void save(String key, MultipartFile businessLicenseImgFile);

    ImageDto find(String key);

    boolean isExisting(String key);
}
