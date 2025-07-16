package io.aksenaksen.demo.usms.video.application.provided;

import java.util.List;

public interface VideoServicePort {

    String getLiveVideo(String streamKey, String protocol, String filename);

    byte[] getReplayVideo(String streamKey, String filename);
    List<String> getReplayVideoFilenames(Long cctvId, String date, String streamKey);
}