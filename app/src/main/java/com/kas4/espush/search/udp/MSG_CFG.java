package com.kas4.espush.search.udp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/29.
 */
public class MSG_CFG {


    String espush = "ESPUSH";// 8
    String ssid = "";// 32 need
    String password = "";// 64 need
    int app_flag = 1;// 4
    int appid = 0;// 4 need
    String appkey = "";// 32 need


    public byte[] buildMSG() {

        List<Byte> bytes = new ArrayList<>();

        List<Byte> b1 = CodeUtil.asciiToBytes(espush, 8);


        List<Byte> b2 = CodeUtil.asciiToBytes(ssid, 32);


        List<Byte> b3 = CodeUtil.asciiToBytes(password, 64);


        List<Byte> b4 = CodeUtil.intToByte(app_flag, 4);


        List<Byte> b5 = CodeUtil.intToByte(appid, 4);


        List<Byte> b6 = CodeUtil.asciiToBytes(appkey, 32);


        bytes.addAll(b1);
        bytes.addAll(b2);
        bytes.addAll(b3);
        bytes.addAll(b4);
        bytes.addAll(b5);
        bytes.addAll(b6);


        return CodeUtil.toByteArray(bytes);

    }

    public String getEspush() {
        return espush;
    }

    public void setEspush(String espush) {
        this.espush = espush;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getApp_flag() {
        return app_flag;
    }

    public void setApp_flag(int app_flag) {
        this.app_flag = app_flag;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }


}
