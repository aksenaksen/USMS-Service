package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.usms.member.application.required.VerificationPort;
import io.aksenaksen.demo.usms.member.application.required.VerificationStatusPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class VerificationServiceTest {

    @Autowired
    VerificationPort verificationPort;

    @Autowired
    VerificationStatusPort verificationStatusPort;

    @Autowired
    VerificationService verificationService;

    String testEmail = "ans109905@naver.com";

    @AfterEach
    void drop(){
        verificationPort.remove(testEmail);
        verificationStatusPort.remove(testEmail);
    }


    @Test
    @DisplayName("본인인증 코드 전송 테스트")
    void sendCode(){
        verificationService.sendCode(testEmail, "email");
        Assertions.assertThat(verificationPort.read(testEmail)).isNotNull();
    }

    @Test
    @DisplayName("본인인증 및 본인인증 상태 생성 테스트")
    void sendVerification(){
        verificationService.sendCode(testEmail,"email");

        Assertions.assertThatThrownBy(() -> verificationService.verifyCode(testEmail,"123452"))
                .isInstanceOf(RuntimeException.class);
        verificationService.verifyCode(testEmail, verificationPort.read(testEmail));
        Assertions.assertThat(verificationStatusPort.read(testEmail)).isNotNull();
    }



}