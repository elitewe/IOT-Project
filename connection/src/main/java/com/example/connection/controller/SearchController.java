package com.example.connection.controller;

import com.example.connection.bean.FormData;
import com.example.connection.bean.Result;
import com.example.connection.exception.FormatException;
import com.example.connection.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping("/add/device")
    public Result addService(@RequestBody FormData formData) {
        return searchService.addDevice(formData);
    }

    @GetMapping("/topicWithClients/all")
    public Result getAllTopicWithClients() {
        return searchService.getAllTopicWithClients();
    }

    @GetMapping("/device/{id}")
    public Result getDeviceById(@PathVariable("id") int id) {
        return searchService.getDeviceById(id);
    }

    @GetMapping("/topicsWithContent/{id}")
    public Result getTopicsWithContentByClientId(@PathVariable("id") int id) {
        return searchService.getTopicsWithContentByClientId(id);
    }

    @GetMapping("/publicKey")
    public Result getPublicKey() {
        return searchService.getPublicKey();
    }

    @GetMapping("/device/all")
    public Result getAllDevice() {
        return searchService.getAllDevices();
    }

    @GetMapping("/contents/all/{topic}")
    public Result getAllContentsByTopic(@PathVariable("topic") String topic) {
        return searchService.getAllContentsByTopic(topic);
    }

    @GetMapping("/topics/all/{id}")
    public Result getAllTopicsByClientId(@PathVariable("id") int id) {
        return searchService.getAllTopicsByClientId(id);
    }

    @GetMapping("/roles/all")
    public Result getAllRoles() {
        return searchService.getAllRoles();
    }

    @GetMapping("/devTypes/all")
    public Result getAllDevTypes() {
        return searchService.getAllDevTypes();
    }

}
