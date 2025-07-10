package io.aksenaksen.demo.notification.application;

public enum NotificationType {
    EMAIL("email", "emailSender"),
    SMS("sms", "smsSender");

    private final String type;
    private final String sender;

    NotificationType(String type, String sender) {
        this.type = type;
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public static NotificationType fromString(String key) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.getType().equalsIgnoreCase(key)) {
                return notificationType;
            }
        }
        throw new IllegalArgumentException("Unknown NotificationType key: " + key);
    }
}
