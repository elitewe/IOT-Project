package com.example.connection.controller;

import com.example.connection.bean.Result;
import com.example.connection.service.MqttService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    private MqttService mqttService;

    @ApiOperation("发布消息")
    @GetMapping("/send")
    public String send(@RequestParam("topic") String topic, @RequestParam("content") String content) {
        return mqttService.send(topic, content);
    }

    @ApiOperation("订阅主题")
    @GetMapping("/subscribe")
    public String subscribe(@RequestParam("topic") String topic, @RequestParam("client") String clientId) {
        return mqttService.subscribe(topic, clientId);
    }

}