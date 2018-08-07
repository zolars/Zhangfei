package com.xyf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.xyf.Element;
import com.xyf.Circuit;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) {

        Circuit t = new Circuit();

        String path = AppTest.class.getClassLoader().getResource("").getPath() + "circuitData.txt";
        path = path.replaceAll("%20", " ");

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));

            String temp;
            while ((temp = br.readLine()) != null) {
                String[] dataArray = temp.split(" ");
                t.add(new Element(dataArray[0], dataArray[1].charAt(0), Integer.parseInt(dataArray[2]),
                        Integer.parseInt(dataArray[3]), Integer.parseInt(dataArray[4]), Integer.parseInt(dataArray[5]),
                        Double.parseDouble(dataArray[6])));

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        t.calculate();
    }
}
