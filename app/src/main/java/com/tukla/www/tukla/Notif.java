package com.tukla.www.tukla;

public class Notif {
    private String senderID;
    private String message;
    private String date;
    private boolean isRead;

    public Notif() {

    }

    public Notif(String senderID, String message, String date, boolean isRead) {
        this.senderID = senderID;
        this.message = message;
        this.date = date;
        this.isRead = isRead;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getDate() {
        return date;
    }
}
