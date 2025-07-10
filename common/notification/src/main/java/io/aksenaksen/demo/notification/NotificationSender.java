package io.aksenaksen.demo.notification;

public interface NotificationSender {

    void send(String destination, String subject, String message);
}
