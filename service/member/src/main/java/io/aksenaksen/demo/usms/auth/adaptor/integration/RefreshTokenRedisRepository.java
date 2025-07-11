package io.aksenaksen.demo.usms.auth.adaptor.integration;

import io.aksenaksen.demo.usms.auth.application.provieded.RefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository implements RefreshTokenPort {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_KEY_FORMAT = "auth::refresh::%s";

    @Override
    public void create(String username, String refreshToken, long ttlMillis) {
        redisTemplate.opsForValue().setIfAbsent(
                generateKey(username),
                refreshToken,
                Duration.ofMillis(ttlMillis)
        );
    }

    @Override
    public void delete(String username) {
        redisTemplate.delete(generateKey(username));
    }

    @Override
    public void reissueToken(String username, String refreshToken, long ttlMillis) {
        String key = generateKey(username);
        long ttlSeconds = Duration.ofMillis(ttlMillis).toSeconds();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection conn = (StringRedisConnection) connection;
            conn.del(key);
            conn.setEx(key, ttlSeconds, refreshToken);
            return null;
        });
    }

    @Override
    public String read(String key) {
        return redisTemplate.opsForValue().get(generateKey(key));
    }

    private String generateKey(String username) {
        return REFRESH_KEY_FORMAT.formatted(username);
    }
}
