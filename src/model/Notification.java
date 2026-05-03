package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;

    public Notification(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    public String getMessage()  { return message; }
    public boolean isRead()     { return isRead; }
    public void markRead()      { this.isRead = true; }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return "[" + getFormattedTime() + "] " + message;
    }
}