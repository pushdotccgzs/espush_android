package com.kas4.espush.entity;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/27.
 */
public class IOEntity extends BaseEntity{

    private String name;
    private int pin;
    private int edge;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
