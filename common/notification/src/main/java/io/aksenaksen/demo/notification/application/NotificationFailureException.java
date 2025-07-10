package io.aksenaksen.demo.notification.application;

public class NotificationFailureException extends RuntimeException{

    public NotificationFailureException(String message){
        super(message);
    }

}