package com.example.connection.controller;

import com.example.connection.bean.Result;
import com.example.connection.mapper.SearchMapper;
import com.example.connection.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/http")
public class HttpController {

    @Autowired
    HttpService httpService;

    @PostMapping(value = "/upload/{id}", produces = "application/json;charset=UTF-8")
    public Result process(@PathVariable("id") int clientId, @RequestParam("roles") String roles, @RequestBody String message) {
        return httpService.process(clientId, message, roles);
    }

    @GetMapping("/get/httpMessage/all")
    public Result getAllHttpMessage() {
        return httpService.getAllMessage();
    }

    @GetMapping("/get/messages/{id}")
    public Result getMessagesByClientId(@PathVariable("id") int id) {
        return httpService.getMessagesByClientId(id);
    }

}
