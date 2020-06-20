package com.example.connection.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormData {
    public String name; //设备名
    public String devType;
    public String roleType;
    public String protocol; //协议名

    public FormData(String name, String devType, String roleType, String protocol) {
        this.name = name;
        this.devType = devType;
        this.roleType = roleType;
        this.protocol = protocol;
    }
}
