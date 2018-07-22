package com.util;

/**
 * @title Coordinate
 * @description 构建二维坐标类型提供支持
 * @author xyf
 * @time 2018-07-07 23:46:20
 */
public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** , 相等返回 true, 否则返回 false */

    /**
     * 判断两个坐标关系
     * 
     * @param a
     * @return boolean 相等为true, 不等为false
     */
    public boolean equals(Coordinate a) {
        if (this.x == a.x && this.y == a.y)
            return true;
        else
            return false;

    }

    @Override
    public String toString() {
        return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
    }

}
