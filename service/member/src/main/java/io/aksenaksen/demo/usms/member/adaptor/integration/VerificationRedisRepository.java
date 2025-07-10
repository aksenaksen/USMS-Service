package io.aksenaksen.demo.usms.member.adaptor.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VerificationRedisRepository {

    private final StringRedisTemplate redisTemplate;
    public static final String VERIFY_KEY_FORMAT = "member::verification::principal::%s";

    public void create(String principal, String code){
        String key = generateKey(principal);
        redisTemplate.opsForValue()
                .set(principal,code);
    }

    private String generateKey(String principal) {
        return VERIFY_KEY_FORMAT.formatted(principal);
    }


}
