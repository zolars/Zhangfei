package com.xyf.rules;

import java.util.ArrayList;

/**
 * @description 建立一个节点表示的无向图
 */
public class Node {
    public String name = null;
    public ArrayList<Node> relationNodes = new ArrayList<Node>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Node> getRelationNodes() {
        return relationNodes;
    }

    public void setRelationNodes(ArrayList<Node> relationNodes) {
        this.relationNodes = relationNodes;
    }
}
