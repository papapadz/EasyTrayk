package com.tukla.www.tukla;

public class Message {
    private User sender;
    private User receiver;
    private String message;
    private String createdAt;

    public Message(){

    }

    public Message(User sender, User receiver, String message, String createdAt) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public User getReceiver() {
        return receiver;
    }

    public User getSender() {
        return sender;
    }
}
