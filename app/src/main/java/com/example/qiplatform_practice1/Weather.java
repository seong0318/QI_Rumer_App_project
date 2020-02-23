package com.example.qiplatform_practice1;

public class Weather {
    float lat;
    float ion;
    String weather;

    public void setLat(float lat){ this.lat = lat;}
    public void setIon(float ion){ this.ion = ion;}
    public void setWeather(String w){ this.weather = w;}

    public String getWeather() { return weather;}
}
