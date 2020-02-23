package com.example.qiplatform_practice1;

public class Dictionary {

    private String sensorid;
    private String macaddress;

    public String getSensorid() {
        return sensorid;
    }

    public void getSensorid(String sensorid) {
        this.sensorid = sensorid;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public Dictionary(String sensorid, String macaddress) {
        this.sensorid = sensorid;
        this.macaddress = macaddress;
    }
}
