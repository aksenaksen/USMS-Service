package io.aksenaksen.demo.notification.application;

public interface NotificationSender {

    void send(String destination, String subject, String message);

    void send(String destination, String message);
}
