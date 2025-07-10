package io.aksenaksen.demo.usms.member.adaptor.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VerificationStatusRedisRepositoryTest {

    @Autowired
    VerificationStatusRedisRepository verificationStatusRedisRepository;

    @Test
    void create(){
        verificationStatusRedisRepository.create("ans109905@naver.com");
        String result = verificationStatusRedisRepository.read("ans109905@naver.com");
        Assertions.assertThat(result).isEqualTo("true");

    }


}