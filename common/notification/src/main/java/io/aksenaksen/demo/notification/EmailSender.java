package io.aksenaksen.demo.notification;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender implements NotificationSender{

    public static final String IDENTIFICATION_MESSAGE= "[Usms] 아래의 인증번호를 입력해주세요\n";
    public static final String EMAIL_SEND_FAILURE_MESSAGE= "이메일 전송에 실패하였습니다.";
    public static final String SMS_SEND_SUCCESS_MESSAGE= "SMS전송에 실패하였습니다.";

    private final JavaMailSender mailSender;
    private final String sender;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.sender = "ans109905@naver.com";
    }

    @Override
    public void send(String destination, String subject, String message) {

        SimpleMailMessage simpleMsg = makeMessage(destination, subject, message);

        try{
            mailSender.send(simpleMsg);
        }catch (MailException exception){
            throw new NotificationFailureException(EMAIL_SEND_FAILURE_MESSAGE);
        }

    }

    private SimpleMailMessage makeMessage(String destination, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(destination);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        return simpleMailMessage;
    }
}
