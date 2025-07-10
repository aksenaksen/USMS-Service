package io.aksenaksen.demo.notification.adaptor;

import io.aksenaksen.demo.notification.application.NotificationFailureException;
import io.aksenaksen.demo.notification.application.NotificationSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class EmailSender implements NotificationSender {

    private static final String IDENTIFICATION_SUBJECT = "[Usms] 아래의 인증번호를 입력해주세요\n";
    private static final String EMAIL_SEND_FAILURE_MESSAGE = "이메일 전송에 실패하였습니다.";
    private static final String EMAIL_SENDER_ADDRESS = "ans109905@naver.com";
    private static final String EMAIL_SUBJECT_HTML = "[Usms] 이메일 인증번호 안내";
    private static final String EMAIL_HTML_TEMPLATE = """
        <div style="font-family: Arial, sans-serif; padding: 20px;">
            <h2 style="color: #4CAF50;">[Usms] 이메일 인증번호</h2>
            <p>아래의 인증번호를 입력해주세요:</p>
            <div style="font-size: 24px; font-weight: bold; margin-top: 10px;">
                %s
            </div>
        </div>
        """;

    private final JavaMailSender mailSender;
    private final String sender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
        this.sender = EMAIL_SENDER_ADDRESS;
    }

    @Override
    public void send(String destination, String subject, String message) {
        SimpleMailMessage simpleMsg = makeMessage(destination, subject, message);
        try {
            mailSender.send(simpleMsg);
        } catch (MailException exception) {
            throw new NotificationFailureException(EMAIL_SEND_FAILURE_MESSAGE);
        }
    }

    @Override
    public void send(String destination, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            helper.setFrom(sender);
            helper.setTo(destination);
            helper.setSubject(EMAIL_SUBJECT_HTML);
            helper.setText(buildHtml(message), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            throw new NotificationFailureException(EMAIL_SEND_FAILURE_MESSAGE);
        }
    }

    private String buildHtml(String code) {
        return EMAIL_HTML_TEMPLATE.formatted(code);
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
