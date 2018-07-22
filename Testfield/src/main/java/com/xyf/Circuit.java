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

    private void goStack(int[][] adjMatrix, Element e) {

        Coordinate head = e.getHeadLocation();
        Coordinate tail = e.getTailLocation();

        stack.add(e);

        adjMatrix[elementsList.indexOf(e)][indexOfNodesList(tail)] = 1;
        adjMatrix[elementsList.indexOf(e)][indexOfNodesList(head)] = -1;

        for (Element etemp : elementsList) {
            if (!stack.contains(etemp)) {
                if (etemp.getHeadLocation().equals(tail)) {
                    goStack(adjMatrix, etemp);
                } else if (etemp.getTailLocation().equals(tail)) {

                    Coordinate t = etemp.getHeadLocation();
                    etemp.setHeadLocation(etemp.getTailLocation());
                    etemp.setTailLocation(t);

                    goStack(adjMatrix, etemp);
                }
            }
        }
    }

    /**
     * 计算电路
     */
    public void calculate() {

        int adjMatrix[][] = new int[elementsList.size()][nodesList.size()];

        goStack(adjMatrix, elementsList.get(0));

        for (int i = 0; i < elementsList.size(); i++) {
            for (int j = 0; j < nodesList.size(); j++) {
                System.out.printf(" %2d ", adjMatrix[i][j]);
            }
            System.out.println();
        }

    }

}