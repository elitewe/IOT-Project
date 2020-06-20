package com.example.connection.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DeviceVO {
    int id;
    String name;
    String typeName;
    String roleName;
    String protocol;
    Date addTime;

    public DeviceVO(int id, String name, String typeName, String roleName, String protocol) {
        this(id, name, typeName, roleName, protocol, null);
    }

    public DeviceVO(int id, String name, String typeName, String roleName, String protocol, Date addTime) {
        this.id = id;
        this.name = name;
        this.typeName = typeName;
        this.roleName = roleName;
        this.protocol = protocol;
        this.addTime = addTime;
    }
}
