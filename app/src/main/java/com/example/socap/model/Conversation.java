package com.example.socap.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Conversation {
    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;
    @SerializedName("from_user_id")
    private int fromUserId;
    @SerializedName("to_user_id")
    private int toUserId;
    @SerializedName("from_user")
    private User fromUser;
    @SerializedName("to_user")
    private User toUser;
    @SerializedName("messages")
    private List<MessageDetails> messages;

    public Conversation(){}

    public Conversation(
            int id,
            String date,
            int fromUserId,
            int toUserId,
            User fromUser,
            User toUser,
            List<MessageDetails> messages
    ) {
        this.id = id;
        this.date = date;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public List<MessageDetails> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDetails> messages) {
        this.messages = messages;
    }
}
