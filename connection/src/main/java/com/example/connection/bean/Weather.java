package com.example.connection.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Weather {
    public int id;
    public String temperature;
    public Date dayTime;
    public String climate; //气候

    @Override
    public String toString() {
        return "id=" + id + ":temperature=" + temperature + ":dayTime=" + dayTime + ":climate=" + climate;
    }

    public static void main(String[] args) {
        List<Weather> weatherList = new ArrayList<>();
        Weather weather = new Weather();
        int i = 0;
        while (i < 10) {
            weather.setId(i);
            weatherList.add(weather);
            i++;
        }
        System.out.println(weatherList.toString());
    }
}
