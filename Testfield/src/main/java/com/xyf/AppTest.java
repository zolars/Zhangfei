package com.xyf;

import com.xyf.Element;
import com.xyf.Circuit;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) {

        Circuit t = new Circuit();

        t.add(new Element("v0", 'v', 0, 0, 0, 1, 5));
        t.add(new Element("w1", 'w', 0, 1, 1, 1, 0));
        t.add(new Element("w4", 'w', 1, 1, 2, 1, 0));
        t.add(new Element("w6", 'w', 1, 0, 2, 0, 0));
        t.add(new Element("r5", 'r', 2, 0, 2, 1, 15));
        t.add(new Element("r2", 'r', 1, 0, 1, 1, 5));
        t.add(new Element("w3", 'w', 0, 0, 1, 0, 0));

        t.calculate();
    }
}
