package com.xyf.recycle.elements;

/**
 * @title Wire
 * @description 描述一个直导线.
 * @author xyf
 * @time 2018-07-09 01:53:48
 */
public class Wire extends Element {

    /** 继承构造器 */
    public Wire(String name, int i1, int j1, int i2, int j2) {
        super(name, i1, j1, i2, j2);
    }

    @Override
    public String toString() {
        return super.getName() + ", w, " + super.getHeadLocation().toString() + ", "
                + super.getTailLocation().toString();
    }

}