package com.example.connection.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MqttMapper {
    int insertTopicWithClientId(String topic, int clientId);

    int insertContentWithTopic(String content, String topic);


}
