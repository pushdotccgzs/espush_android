package com.kas4.espush.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/26.
 */
public class AppTypeListEntity {
    private List<AppTypeEntity> list=new ArrayList<>();

    public List<AppTypeEntity> getList() {
        return list;
    }

    public void setList(List<AppTypeEntity> list) {
        this.list = list;
    }
}
