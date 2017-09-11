package com.example.mybluetooth;

/**
 * Created by 张豫耀 on 2017/9/11.
 */

public class Device {
    private String name;
    private String address;
    private String judge;

    public Device(String name, String address, String judge) {
        this.name = name;
        this.address = address;
        this.judge = judge;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
