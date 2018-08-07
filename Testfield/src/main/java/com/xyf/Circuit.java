package com.xyf;

import java.util.ArrayList;

import com.util.Coordinate;
import com.xyf.Element;

/**
 * Circuit
 */
public class Circuit {

    ArrayList<Element> elementsList = new ArrayList<>();
    ArrayList<Coordinate> nodesList = new ArrayList<>();
    ArrayList<Element> stack = new ArrayList<>();

    ArrayList<ArrayList<Double>> zMatrix = new ArrayList<>();
    ArrayList<Double> iMatrix = new ArrayList<>();

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

    // private void goAdjStack(int[][] adjMatrix, Element e) {

    // Coordinate head = e.getHeadLocation();
    // Coordinate tail = e.getTailLocation();

    // stack.add(e);

    // adjMatrix[elementsList.indexOf(e)][indexOfNodesList(tail)] = 1;
    // adjMatrix[elementsList.indexOf(e)][indexOfNodesList(head)] = -1;

    // for (Element etemp : elementsList) {
    // if (!stack.contains(etemp)) {
    // if (etemp.getHeadLocation().equals(tail)) {
    // goAdjStack(adjMatrix, etemp);
    // } else if (etemp.getTailLocation().equals(tail)) {

    // Coordinate t = etemp.getHeadLocation();
    // etemp.setHeadLocation(etemp.getTailLocation());
    // etemp.setTailLocation(t);

    // goAdjStack(adjMatrix, etemp);
    // }
    // }
    // }
    // }

    public void linearCalculate(ArrayList<ArrayList<Double>> left, ArrayList<Double> right) {

    }

    /**
     * 计算电路
     */
    public void calculate() {

        /**
         * 临接矩阵: 行数为元件数, 列数为节点数. 每行仅分布两个值: 1 & -1, 其余均为0. 用于计算每个节点流入和流出的电流守恒.
         */
        // int adjMatrix[][] = new int[elementsList.size()][nodesList.size()];
        // goAdjStack(adjMatrix, elementsList.get(0));

        for (int i = 0; i < nodesList.size(); i++) {
            ArrayList<Double> columnList = new ArrayList<>();
            for (int j = 0; j < nodesList.size(); j++) {
                columnList.add(j, 0.0);
            }
            zMatrix.add(i, columnList);
            iMatrix.add(i, 0.0);
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
            // 生成阻抗矩阵
            switch (etemp.getType()) {
            case 'i': {
                iMatrix.set(x, -etemp.getValue());
                iMatrix.set(y, etemp.getValue());
                break;
            }
            case 'v': {
                break;
            }
            case 'w': {
                break;
            }
            case 'r': {
                zMatrix.get(x).set(x, zMatrix.get(x).get(x) + 1 / etemp.getValue());
                zMatrix.get(y).set(y, zMatrix.get(y).get(y) + 1 / etemp.getValue());
                zMatrix.get(x).set(y, -1 / etemp.getValue());
                zMatrix.get(y).set(x, -1 / etemp.getValue());
                break;
            }
            case 'g': {
                zMatrix.get(zMatrix.size() - 1).set(x, 1.0);
                for (int i = 0; i < zMatrix.size(); i++) {
                    zMatrix.get(i).remove(zMatrix.get(i).size() - 1);
                }
                break;
            }

            default:
                break;
            }

        }

        for (int i = 0; i < zMatrix.size(); i++) {
            for (int j = 0; j < zMatrix.get(i).size(); j++) {
                System.out.printf(" %1.4f,", zMatrix.get(i).get(j));
            }
            System.out.println();
        }

        for (int i = 0; i < nodesList.size(); i++) {
            System.out.printf(" %1.4f\n", iMatrix.get(i));
        }

        linearCalculate(zMatrix, iMatrix);

    }

}