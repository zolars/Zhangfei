package com.xyf;

import com.util.*;

/**
 * @title Element
 * @description 元件类
 * @author xyf
 * @time 2018-07-09 00:07:54
 */
public class Element {
    private String name; // 标识
    private char type; // 元件类型
    private Coordinate head, tail; // 位置坐标
    private double value; // 电压值, 电阻值...

    /** 初始化元件标识 */
    public Element(String name, char type, int i1, int j1, int i2, int j2, double value) {
        this.name = name;
        this.type = type;
        head = new Coordinate(i1, j1);
        tail = new Coordinate(i2, j2);
        this.value = value;
    }

    /** 设置标识 */
    public void setName(String name) {
        this.name = name;
    }

    /** 获取标识 */
    public String getName() {
        return name;
    }

    /** 设置标识 */
    public void setType(char type) {
        this.type = type;
    }

    /** 获取标识 */
    public char getType() {
        return type;
    }

    /** 设置头坐标 */
    public void setHeadLocation(Coordinate head) {
        this.head = head;
    }

    /** 获取头坐标 */
    public Coordinate getHeadLocation() {
        return head;
    }

    /** 设置尾坐标 */
    public void setTailLocation(Coordinate tail) {
        this.tail = tail;
    }

    /** 获取尾坐标 */
    public Coordinate getTailLocation() {
        return tail;
    }

    /** 设置电压值, 电阻值... */
    public void setValue(int value) {
        this.value = value;
    }

    /** 获取电压值, 电阻值... */
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (type) {
        case 'w':
            return getName() + ", w, " + getHeadLocation().toString() + ", " + getTailLocation().toString();

        case 'r':
            return getName() + ", r, " + getHeadLocation().toString() + ", " + getTailLocation().toString() + ", "
                    + value;

        case 'v':
            return getName() + ", v, " + getHeadLocation().toString() + ", " + getTailLocation().toString() + ", "
                    + value;

        case 'g':
            return getName() + ", g, " + getHeadLocation().toString();

        default:
            return "Error";
        }
    }

}