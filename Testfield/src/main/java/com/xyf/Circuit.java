package com.xyf;

import java.util.ArrayList;

import Jama.Matrix;

import com.util.Coordinate;
import com.xyf.Element;

/**
 * Circuit
 */
public class Circuit {

    ArrayList<Element> elementsList = new ArrayList<>();
    ArrayList<Coordinate> nodesList = new ArrayList<>();
    ArrayList<Element> stack = new ArrayList<>();

    ArrayList<ArrayList<Double>> zArrayList = new ArrayList<>();
    ArrayList<Double> iArrayList = new ArrayList<>();

    /**
     * 初始化
     */
    public Circuit() {

    }

    /**
     * 找到nodesList中特定元素的下标
     */
    public int indexOfNodesList(Coordinate nodep) {
        int i;
        for (i = 0; i < nodesList.size(); i++) {
            if (nodesList.get(i).equals(nodep)) {
                break;
            }
        }
        return i;
    }

    /**
     * 添加元件
     */
    public void add(Element e) {
        elementsList.add(e);

        boolean headKey = true, tailKey = true;
        for (Coordinate nodep : nodesList) {
            if (e.getHeadLocation().equals(nodep)) {
                headKey = false;
            }
            if (e.getTailLocation().equals(nodep)) {
                tailKey = false;
            }
        }

        if (headKey) {
            nodesList.add(e.getHeadLocation());
        }
        if (tailKey) {
            nodesList.add(e.getTailLocation());
        }

    }

    /**
     * 计算电路
     */
    public void calculate() {
        for (int i = 0; i < nodesList.size(); i++) {
            ArrayList<Double> columnList = new ArrayList<>();
            for (int j = 0; j < nodesList.size(); j++) {
                columnList.add(j, 0.0);
            }
            zArrayList.add(i, columnList);
            iArrayList.add(i, 0.0);
        }

        for (Element etemp : elementsList) {
            int x = -1, y = -1; // 记录录入矩阵的行列
            for (int i = 0; i < nodesList.size(); i++) {
                if (x != -1 && y != -1)
                    break;
                else if (nodesList.get(i).equals(etemp.getHeadLocation())) {
                    x = i;
                } else if (nodesList.get(i).equals(etemp.getTailLocation())) {
                    y = i;
                }
            }
            // 生成阻抗矩阵 ArrayList格式
            switch (etemp.getType()) {
            case 'i': {
                iArrayList.set(x, -etemp.getValue());
                iArrayList.set(y, etemp.getValue());
                break;
            }
            case 'v': {
                break;
            }
            case 'w': {
                break;
            }
            case 'r': {
                zArrayList.get(x).set(x, zArrayList.get(x).get(x) + 1 / etemp.getValue());
                zArrayList.get(y).set(y, zArrayList.get(y).get(y) + 1 / etemp.getValue());
                zArrayList.get(x).set(y, -1 / etemp.getValue());
                zArrayList.get(y).set(x, -1 / etemp.getValue());
                break;
            }
            case 'g': {
                zArrayList.get(zArrayList.size() - 1).set(x, 1.0);
                for (int i = 0; i < zArrayList.size(); i++) {
                    zArrayList.get(i).remove(zArrayList.get(i).size() - 1);
                }
                break;
            }
            default:
                break;
            }
        }

        // 生成阻抗矩阵 Matrix格式
        Matrix zMatrix = new Matrix(zArrayList);

        double[] itemp = new double[iArrayList.size()];
        for (int i = 0; i < itemp.length; i++) {
            itemp[i] = iArrayList.get(i);
        }
        Matrix iMatrix = new Matrix(itemp, itemp.length);

        // 解线性方程组
        Matrix ansMatrix = zMatrix.solve(iMatrix);

        // 显示解
        ansMatrix.print(3, 3);

    }

}