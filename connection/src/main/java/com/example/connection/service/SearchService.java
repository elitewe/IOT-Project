package com.example.connection.service;

import com.example.connection.bean.*;
import com.example.connection.bean.po.DevicePO;
import com.example.connection.bean.vo.DeviceVO;
import com.example.connection.exception.FormatException;
import com.example.connection.mapper.SearchMapper;
import com.example.connection.mqtt.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    SearchMapper searchMapper;

    public void setSearchMapper(SearchMapper searchMapper) {
        this.searchMapper = searchMapper;
    }

    public Result addDevice(FormData formData) {
        if (!isRightProtocol(formData.getProtocol())) {
            throw new FormatException("协议类型不支持!");
        }
        Result<DeviceVO> result = new Result();
        int id = getInsertId(formData);
        DeviceVO deviceVO = new DeviceVO(id, formData.getName(), formData.getDevType(), formData.getRoleType(), formData.getProtocol());
        result.setSuccess(true);
        result.setDetail(deviceVO);
        return result;
    }

    private int getInsertId(FormData formData){
        searchMapper.insertDevice(formData);
        return searchMapper.searchMaxId();
    }

    public Result getAllTopicWithClients() {

        Result result = new Result();
        List<TopicWithClient> topicWithClients = searchMapper.getAllTopicWithClients();
        result.setDetail(topicWithClients);
        result.setSuccess(true);

        return result;
    }

    public Result getDeviceById(int id) {
        DevicePO devicePO = searchMapper.getDeviceById(id);
        String roleName = searchMapper.getRoleNameById(devicePO.getRoleId());
        String typeName = searchMapper.getDeviceTypeById(devicePO.getTypeId());
        DeviceVO deviceVO = new DeviceVO(devicePO.getId(), devicePO.getName(), typeName, roleName, devicePO.getProtocol(), devicePO.getAddTime());

        Result result = new Result();
        result.setSuccess(true);
        result.setDetail(deviceVO);
        return result;
    }

    public Result getTopicsWithContentByClientId(int id) {
        Result result = new Result();
        result.setDetail(searchMapper.getTopicsWithContentByClientId(id));
        result.setSuccess(true);
        return result;
    }

    public Result getPublicKey() {
        Result result = new Result();
        result.setSuccess(true);
        String publicKey = searchMapper.getPublicKey();
        result.setDetail(publicKey);
        return result;
    }

    public Result getAllDevices() {
        List<DevicePO> devicePOList = searchMapper.getAllDevices();
        List<DeviceVO> deviceVOList = new ArrayList<>();
        for (DevicePO devicePO : devicePOList) {
            DeviceVO deviceVO = toVO(devicePO);
            deviceVOList.add(deviceVO);
        }
        Result result = new Result();
        result.setDetail(deviceVOList);
        result.setSuccess(true);
        return result;
    }

    public Result getAllContentsByTopic(String topic) {
        List<ContentWithDate> contents = searchMapper.getAllContentsByTopic(topic);
        Result result = new Result();
        result.setSuccess(true);
        result.setDetail(contents);
        return result;
    }

    public Result getAllTopicsByClientId(int id) {
        List<String> topics = searchMapper.getAllTopicsByClientId(id);
        Result result = new Result();
        result.setSuccess(true);
        result.setDetail(topics);
        return result;
    }

    public Result getAllRoles() {
        Result result = new Result();
        result.setSuccess(true);
        result.setDetail(searchMapper.getAllDevRoles());
        return result;
    }

    public Result getAllDevTypes() {
        Result result = new Result();
        result.setSuccess(true);
        result.setDetail(searchMapper.getAllDevTypes());
        return result;
    }

    public DeviceVO toVO(DevicePO devicePO) {
        String role = transRole(devicePO.getRoleId(), searchMapper.getAllDevRoles());
        String type = transType(devicePO.getTypeId(), searchMapper.getAllDevTypes());
        return new DeviceVO(devicePO.getId(), devicePO.getName(), type, role, devicePO.getProtocol(), devicePO.getAddTime());
    }

    private String transRole(int id, List<DevRole> list) {
        for (DevRole devRole : list) {
            if (devRole.id == id) {
                return devRole.roleName;
            }
        }
        return "";
    }

    private String transType(int id, List<DevType> list) {
        for (DevType devType : list) {
            if (devType.id == id) {
                return devType.typeName;
            }
        }
        return "";
    }

    private boolean isRightProtocol(String protocol) {
        return protocol.equals("HTTP") || protocol.equals("MQTT") || protocol.equals("COAP");
    }

}

