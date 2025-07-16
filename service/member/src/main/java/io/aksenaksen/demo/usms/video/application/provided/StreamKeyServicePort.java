package io.aksenaksen.demo.usms.video.application.provided;

public interface StreamKeyServicePort {
    void checkAuthOfPushingStream(String streamKey, long connectedTime);
}
