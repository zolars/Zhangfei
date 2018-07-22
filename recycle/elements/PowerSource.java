package com.xyf.recycle.elements;

/**
 * @title PowerSource
 * @description 描述一个电压源, 包含电压
 * @author xyf
 * @time 2018-07-07 23:14:59
 */
public class PowerSource extends Element {

    private int value; // 电压值

    /** 继承构造器 */
    public PowerSource(String name, int i1, int j1, int i2, int j2, int value) {
        super(name, i1, j1, i2, j2);
        this.value = value;
    }

    /** 设置电压值 */
    public void setValue(int value) {
        this.value = value;
    }

    /** 获取电压值 */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.getName() + ", v, " + super.getHeadLocation().toString() + ", "
                + super.getTailLocation().toString() + ", " + value;
    }

}