package com.example.connection.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessContent {
    public int clientId; //设备id
    public String payload;
    public String url;
    public String messageId; //消息标识符
    public String protocol;

    public ProcessContent(int clientId, String payload, String url, String messageId, String protocol) {
        this.clientId = clientId;
        this.payload = payload;
        this.url = url;
        this.messageId = messageId;
        this.protocol = protocol;
    }
}
