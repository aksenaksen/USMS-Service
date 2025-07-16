package io.aksenaksen.demo.usms.video.adaptor.integration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import io.aksenaksen.demo.usms.video.application.required.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.aksenaksen.demo.usms.config.CacheConfiguration.FILE_NAME_LIST_CACHE_KEY;


@Repository
@RequiredArgsConstructor
public class AwsS3VideoRepository implements VideoRepository {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.transcode-video-bucket}")
    private String transcodeVideoBucket;

    @Override
    public byte[] getVideo(String key) {

        S3Object s3Object = amazonS3.getObject(transcodeVideoBucket, key);
        try {
            return s3Object.getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = FILE_NAME_LIST_CACHE_KEY, key =  "#path", cacheManager = "cacheManager")
    @Override
    public List<String> getVideoFilenames(String path) {

        return amazonS3.listObjects(transcodeVideoBucket, path)
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .filter(key-> key.endsWith(".m3u8"))
                .map(key -> {
                    int idx = key.lastIndexOf("/");
                    return key.substring(idx+1);
                })
                .collect(Collectors.toList());
    }
}