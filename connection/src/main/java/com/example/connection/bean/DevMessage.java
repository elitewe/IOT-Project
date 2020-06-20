package com.example.connection.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DevMessage {
    public int clientId;
    public String devType;
    public String devName;
    public String payload;
    public String protocol;
    public String url;
    public Date addTime;

    public DevMessage() {

    }

    public DevMessage(int clientId, String devType, String devName, String payload, String protocol, String url, Date addTime) {
        this.clientId = clientId;
        this.devType = devType;
        this.devName = devName;
        this.payload = payload;
        this.protocol = protocol;
        this.url = url;
        this.addTime = addTime;
    }
}
