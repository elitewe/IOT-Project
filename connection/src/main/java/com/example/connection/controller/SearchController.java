package com.example.connection.controller;

import com.example.connection.bean.FormData;
import com.example.connection.bean.Result;
import com.example.connection.exception.FormatException;
import com.example.connection.service.SearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @ApiOperation("添加设备")
    @PostMapping("/add/device")
    public Result addService(@RequestBody FormData formData) {
        return searchService.addDevice(formData);
    }

    @ApiOperation("获取所有主题以及对应的消息")
    @GetMapping("/topicWithClients/all")
    public Result getAllTopicWithClients() {
        return searchService.getAllTopicWithClients();
    }

    @ApiOperation("根据id获取设备信息")
    @GetMapping("/device/{id}")
    public Result getDeviceById(@PathVariable("id") int id) {
        return searchService.getDeviceById(id);
    }

    @ApiOperation("根据id获取设备订阅的主题及内容")
    @GetMapping("/topicsWithContent/{id}")
    public Result getTopicsWithContentByClientId(@PathVariable("id") int id) {
        return searchService.getTopicsWithContentByClientId(id);
    }

    @ApiOperation("获取公钥")
    @GetMapping("/publicKey")
    public Result getPublicKey() {
        return searchService.getPublicKey();
    }

    @ApiOperation("获取所有设备")
    @GetMapping("/device/all")
    public Result getAllDevice() {
        return searchService.getAllDevices();
    }

    @ApiOperation("获取指定主题的所有内容")
    @GetMapping("/contents/all/{topic}")
    public Result getAllContentsByTopic(@PathVariable("topic") String topic) {
        return searchService.getAllContentsByTopic(topic);
    }

    @ApiOperation("获取指定设备的所有订阅主题")
    @GetMapping("/topics/all/{id}")
    public Result getAllTopicsByClientId(@PathVariable("id") int id) {
        return searchService.getAllTopicsByClientId(id);
    }

    @ApiOperation("获取所有角色信息")
    @GetMapping("/roles/all")
    public Result getAllRoles() {
        return searchService.getAllRoles();
    }

    @ApiOperation("获取所有设备类型")
    @GetMapping("/devTypes/all")
    public Result getAllDevTypes() {
        return searchService.getAllDevTypes();
    }

    @ApiOperation("获取所有核心数据")
    @GetMapping("/cores/all")
    public Result getAllCores() {
        return searchService.getAllCores();
    }
}
