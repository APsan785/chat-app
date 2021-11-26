package com.example.firebase1.models;

import java.security.Timestamp;

public class Messages {
    String text, receiverId, senderId;
    Long timeStamp;
    int hours, mins;

    public Messages(String text, String receiverId, String senderId, Long timeStamp, int hours, int mins) {
        this.text = text;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
        this.hours = hours;
        this.mins = mins;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public Messages() {
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
