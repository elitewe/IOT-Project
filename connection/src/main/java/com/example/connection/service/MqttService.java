package com.example.connection.service;


import com.example.connection.exception.ServiceException;
import com.example.connection.mapper.MqttMapper;
import com.example.connection.mqtt.Client;
import com.example.connection.mqtt.Server;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    @Autowired
    MqttMapper mqttMapper;

    public void setMqttMapper(MqttMapper mqttMapper) {
        this.mqttMapper = mqttMapper;
    }

    public String send(String topic, String content) {
        try {
            Server server = new Server();
            server.setContent(topic, content);
            server.sendMessage();
        } catch (MqttException e) {
            throw new ServiceException("Mqtt服务器未连接");
        }

        mqttMapper.insertContentWithTopic(topic, content); //保存到数据库

        return "send success";
    }

    public String subscribe(String topic, String clientId) {
        if (Integer.parseInt(clientId) <= 0) {
            throw new ServiceException("clientId不存在");
        }
        Client client = new Client(clientId);
        client.subscribe(topic);
        mqttMapper.insertTopicWithClientId(topic, Integer.parseInt(clientId));
        return "receive success";
    }
}
