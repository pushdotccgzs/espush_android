package com.kas4.espush.entity;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/26.
 */
public class DeviceEntity extends BaseEntity {

//    {
//        "name":"HomeDev", "online":[{
//        "dev_id":"c725c2c0441911e59a718f35a878241f", "flashmap":2, "name":
//        "\u6a21\u62df\u5668", "devid":10492718, "second_boot":1, "appid":15104, "vertype":
//        1, "boot_app":1, "latest":"2015-08-24 21:43:30"
//    }]}

    String dev_id;
    String flashmap;
    String name;
    String devid;
    String second_boot;
    String appid;
    String vertype;
    String boot_app;
    String lastest;


    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public String getFlashmap() {
        return flashmap;
    }

    public void setFlashmap(String flashmap) {
        this.flashmap = flashmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getSecond_boot() {
        return second_boot;
    }

    public void setSecond_boot(String second_boot) {
        this.second_boot = second_boot;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getVertype() {
        return vertype;
    }

    public void setVertype(String vertype) {
        this.vertype = vertype;
    }

    public String getBoot_app() {
        return boot_app;
    }

    public void setBoot_app(String boot_app) {
        this.boot_app = boot_app;
    }

    public String getLastest() {
        return lastest;
    }

    public void setLastest(String lastest) {
        this.lastest = lastest;
    }

}
