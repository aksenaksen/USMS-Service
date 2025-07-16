package io.aksenaksen.demo.usms.store.application.provided;

import io.aksenaksen.demo.usms.store.application.required.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageCommandService {

    private final ImageRepository imageRepository;
}
