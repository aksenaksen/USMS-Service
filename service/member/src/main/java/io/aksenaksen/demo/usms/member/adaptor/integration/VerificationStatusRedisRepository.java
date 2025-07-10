package io.aksenaksen.demo.usms.member.adaptor.integration;

import io.aksenaksen.demo.usms.member.application.required.VerificationStatusPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class VerificationStatusRedisRepository implements VerificationStatusPort {

    private final StringRedisTemplate redisTemplate;
    private static final String VERIFY_STATUS_KEY = "verification::%s";
    private static final String VERIFY_STATUS = "true";

    private static final long TTL = 60 * 3;

    public void create(String principal){
        String key = generateKey(principal);
        redisTemplate.opsForValue().set(key, VERIFY_STATUS, TTL, TimeUnit.SECONDS);
    }

    public String read(String principal){
        return redisTemplate.opsForValue().get(generateKey(principal));
    }

    public void remove(String principal){
        redisTemplate.delete(generateKey(principal));
    }

    private String generateKey(String principal) {
        return VERIFY_STATUS_KEY.formatted(principal);
    }
}
