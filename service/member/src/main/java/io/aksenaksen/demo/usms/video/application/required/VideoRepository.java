package io.aksenaksen.demo.usms.video.application.required;

import java.util.List;

public interface VideoRepository {

    byte[] getVideo(String key);

    List<String> getVideoFilenames(String path);
}
