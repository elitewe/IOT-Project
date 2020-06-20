package com.example.connection.bean.po;

import com.example.connection.bean.DevRole;
import com.example.connection.bean.DevType;
import com.example.connection.bean.vo.DeviceVO;
import com.example.connection.mapper.SearchMapper;
import com.example.connection.service.SearchService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Component
public class DevicePO {
    int id;
    String name;
    int typeId;
    int roleId;
    String protocol;
    Date addTime;

    public DevicePO() {

    }

    public DevicePO(int id, String name, int typeId, int roleId, String protocol, Date addTime) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.roleId = roleId;
        this.protocol = protocol;
        this.addTime = addTime;
    }
}
