package io.aksenaksen.demo.usms.member.adaptor.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VerificationRedisRepositoryTest {

    @Autowired
    VerificationRedisRepository verificationRedisRepository;



    @Test
    void create(){
        String code = makeCode();
        verificationRedisRepository.create("ans109905@naver.com",code);
        String result = verificationRedisRepository.read("ans109905@naver.com");

        Assertions.assertThat(result).isEqualTo(code);


    }

    private String makeCode() {
        Random random = new Random();
        int num = 100000 + random.nextInt(900000);
        String code = String.valueOf(num);
        return code;
    }

}