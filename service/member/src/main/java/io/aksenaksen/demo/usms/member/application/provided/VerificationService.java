package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.notification.application.NotificationSender;
import io.aksenaksen.demo.notification.application.NotificationType;
import io.aksenaksen.demo.snowflake.Snowflake;
import io.aksenaksen.demo.usms.member.application.exception.VerificationCodeMismatchException;
import io.aksenaksen.demo.usms.member.application.required.VerificationPort;
import io.aksenaksen.demo.usms.member.application.required.VerificationStatusPort;
import io.aksenaksen.demo.usms.member.domain.vo.VerificationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationPort verificationPort;
    private final VerificationStatusPort verificationStatusPort;
    private final Map<String, NotificationSender> notificationSenders;

    public void sendCode(String principal , String type){
        VerificationCode verification = VerificationCode.createCode(principal);
        verificationPort.create(verification.principal(), verification.code());

        // mail or sms 보내야한다.
        notificationSenders.get(NotificationType.fromString(type).getSender())
                .send(principal, verification.code());
    }

    public void verifyCode(String principal, String inputCode){
        String code = verificationPort.read(principal);

        VerificationCode verification = VerificationCode.createCode(principal, code);
        if(verification.verify(inputCode)){
            verificationStatusPort.create(principal);
            return;
        }

        throw new VerificationCodeMismatchException("인증번호가 일치하지 않습니다.");
    }

    public boolean isVerified(String principal){
        return verificationStatusPort.read(principal).equals("true");
    }
}
