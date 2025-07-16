package io.aksenaksen.demo.usms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfiguration {

    public static final String FILE_NAME_LIST_CACHE_KEY = "VIDEO_FILE_NAME_LIST_KEY";
    public static final String IMG_FILE_CACHE_KEY = "IMG_FILE_KEY";

    @Value("${usms.cache.transcode-filename.ttl}")
    private long filenameListTtl;

    @Value("${usms.cache.img-file.ttl}")
    private long imgFileTtl;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Map<String , RedisCacheConfiguration> cacheConfigs = new HashMap<>();

        cacheConfigs.put(FILE_NAME_LIST_CACHE_KEY, createCacheConfig(Duration.ofSeconds(filenameListTtl)));
        cacheConfigs.put(IMG_FILE_CACHE_KEY, createCacheConfig(Duration.ofSeconds(imgFileTtl)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(createCacheConfig(Duration.ofMinutes(1)))
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
    private RedisCacheConfiguration createCacheConfig(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(ttl);
    }

}
