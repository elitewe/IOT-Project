package com.example.connection.service;

import com.example.connection.exception.ServiceException;
import com.example.connection.mapper.MqttMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


//@RunWith(SpringRunner.class)
@SpringBootTest
class MqttServiceTest {

    @Test
    void send1() {
        MqttMapper mockMapper = mock(MqttMapper.class);
        MqttService mqttService = new MqttService();
        mqttService.setMqttMapper(mockMapper);
        assertThrows(ServiceException.class, () -> {
            mqttService.send("English", "haha");
        });
    }

    @Test
    void send2() {
        MqttMapper mockMapper = mock(MqttMapper.class);
        MqttService mqttService = new MqttService();
        mqttService.setMqttMapper(mockMapper);
        assertEquals("send success", mqttService.send("English", "hehe"));
    }

    @Test
    void subscribe1() {
        MqttMapper mockMapper = mock(MqttMapper.class);
        MqttService mqttService = new MqttService();
        mqttService.setMqttMapper(mockMapper);
        assertThrows(ServiceException.class, () -> {
            mqttService.subscribe("topic", "-1");
        });
    }

    @Test
    void subscribe2() {
        MqttMapper mockMapper = mock(MqttMapper.class);
        MqttService mqttService = new MqttService();
        mqttService.setMqttMapper(mockMapper);
        assertEquals("receive success", mqttService.subscribe("topic", "32"));
    }
}