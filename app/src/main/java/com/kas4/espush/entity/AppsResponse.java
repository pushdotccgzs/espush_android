package com.kas4.espush.entity;

import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/26.
 */
public class AppsResponse extends BaseEntity {

    private String name;
    private List<DeviceEntity> online;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeviceEntity> getOnline() {
        return online;
    }

    public void setOnline(List<DeviceEntity> online) {
        this.online = online;
    }
}
