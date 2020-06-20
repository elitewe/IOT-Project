package com.example.connection.controller;

import com.example.connection.bean.Result;
import com.example.connection.mapper.SearchMapper;
import com.example.connection.service.HttpService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/http")
public class HttpController {

    @Autowired
    HttpService httpService;

    @ApiOperation("解析数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "设备id"),
            @ApiImplicitParam(name = "roles", value = "RSA加密后角色"),
    })
    @PostMapping(value = "/upload/{id}", produces = "application/json;charset=UTF-8")
    public Result process(@PathVariable("id") int clientId, @RequestParam("roles") String roles, @RequestBody String message) {
        return httpService.process(clientId, message, roles);
    }

    @ApiOperation("获取所有上传数据")
    @GetMapping("/get/httpMessage/all")
    public Result getAllHttpMessage() {
        return httpService.getAllMessage();
    }

    @ApiOperation("根据clientId获取消息")
    @GetMapping("/get/messages/{id}")
    public Result getMessagesByClientId(@PathVariable("id") int id) {
        return httpService.getMessagesByClientId(id);
    }

}
