package com.example.connection.mapper;

import com.example.connection.bean.*;
import com.example.connection.bean.po.DevicePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    int insertDevice(@Param("formData") FormData formData);

    int searchMaxId();

    DevicePO getDeviceById(int id);

    List<DevicePO> getAllDevices();

    String getRoleNameById(int id);

    String getDeviceTypeById(Integer id);
    //每个topic有哪些client订阅
    List<TopicWithClient> getAllTopicWithClients();

    List<String> getTopicsWithContentByClientId(int id);

    String getPublicKey();

    String getPrivateKey();

    List<Resource> getAllResource();

    List<DevRole> getAllDevRoles();

    List<DevType> getAllDevTypes();

    List<Weather> getAllWeathers();

    List<ContentWithDate> getAllContentsByTopic(String topic);
    //获取设备所有的订阅主题
    List<String> getAllTopicsByClientId(int id);

}
