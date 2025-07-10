package io.aksenaksen.demo.usms.member.adaptor.integration;

import io.aksenaksen.demo.usms.member.application.required.VerificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class VerificationRedisRepository implements VerificationPort {

    private final StringRedisTemplate redisTemplate;
    public static final String VERIFY_KEY_FORMAT = "member::verification::principal::%s";

    public static final long TTL = 60;

    public void create(String principal, String code){
        String key = generateKey(principal);
        redisTemplate.opsForValue()
                .set(key ,code , TTL, TimeUnit.SECONDS);
    }

    public String read(String principal){

        return redisTemplate.opsForValue().get(generateKey(principal));
    }

    public void remove(String principal){

        redisTemplate.delete(generateKey(principal));
    }

    private String generateKey(String principal) {
        return VERIFY_KEY_FORMAT.formatted(principal);
    }


}
