package com.example.connection.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {
    private String msg;
    private boolean success;
    private T detail;
}
