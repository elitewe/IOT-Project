package com.example.connection.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resource {
    public int id;
    public String url;
    public String roles;

    public Resource() {

    }

    public Resource(int id, String url, String roles) {
        this.id = id;
        this.url = url;
        this.roles = roles;
    }
}
