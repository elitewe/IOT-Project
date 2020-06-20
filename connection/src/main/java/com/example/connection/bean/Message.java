package com.example.connection.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Message {
    public String payload;
    public Date addTime;

    public Message() {

    }

    public Message(String payload, Date addTime) {
        this.payload = payload;
        this.addTime = addTime;
    }
}
