package com.example.connection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping(value = {"/index", "/index.html"})
    public String routeIndex() {
        return "index";
    }

    @GetMapping(value = {"/application","/application.html"})
    public String routeApplication() {
        return "application";
    }

    @GetMapping(value = {"/hello", "/hello.html"})
    public String routeHello() {
        return "hello";
    }

    @GetMapping(value = {"/details", "/details.html"})
    public String routeDetails() {
        return "details";
    }

    @GetMapping(value = {"/collection", "/collection.html"})
    public String routeCollections() {
        return "collection";
    }

    @GetMapping(value = {"/messages", "/messages.html"})
    public String routeMessages() {
        return "messages";
    }

    @GetMapping(value = {"/content", "/content.html"})
    public String routeContent() {
        return "content";
    }
}
