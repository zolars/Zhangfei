package com.xyf.recycle.elements;

/**
 * @title Resistance
 * @description 描述一个电阻, 包括阻值.
 * @author xyf
 * @time 2018-07-07 23:04:25
 */
public class Resistance extends Element {

    private int value; // 阻值

    /** 继承构造器 */
    public Resistance(String name, int i1, int j1, int i2, int j2, int value) {
        super(name, i1, j1, i2, j2);
        this.value = value;
    }

    /** 设置阻值 */
    public void setValue(int value) {
        this.value = value;
    }

    /** 获取阻值 */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.getName() + ", r, " + super.getHeadLocation().toString() + ", "
                + super.getTailLocation().toString() + ", " + value;
    }

}