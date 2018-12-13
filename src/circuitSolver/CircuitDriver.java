package circuitSolver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CircuitDriver {
    private static final String folderName = "C:\\Users\\DELL\\Desktop";
    private static final String imageFolderName = folderName + "\\image";
    private static final String outputFileName = folderName + "\\CircuitOutput.txt";

    private static final double timeAll = 5;
    private static final double timeStep = 1E-1;

    private static void showMeTheTables(Result[][] table, int[][] detected, int[][] circuitTable) {
        // show me the tables
        System.out.println("The original matrix <table.name> is as below:");
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                System.out.print(table[i][j].getName() + "\t");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("The node matrix <detected> is as below:");
        for (int i = 0; i < detected.length; i++) {
            for (int j = 0; j < detected[0].length; j++) {
                System.out.print(detected[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("The circuit paths <circuitTable> is as below:");
        for (int i = 0; i < circuitTable.length; i++) {
            for (int j = 0; j < circuitTable[0].length; j++) {
                System.out.print(circuitTable[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Result> testList1 = new ArrayList<>();
        ArrayList<Result> testList2 = new ArrayList<>();

        // test-1
        testList1.add(new Result(0, 0, "wl4", 0));
        testList1.add(new Result(0, 2, "v0", 10));
        testList1.add(new Result(0, 4, "wl1", 0));
        testList1.add(new Result(1, 0, "wt4", 0));
        testList1.add(new Result(1, 1, "r0", 2));
        testList1.add(new Result(1, 3, "r2", 2));
        testList1.add(new Result(1, 4, "wt2", 0));
        testList1.add(new Result(2, 0, "wl3", 0));
        testList1.add(new Result(2, 1, "r1", 20));
        testList1.add(new Result(2, 3, "r3", 20));
        testList1.add(new Result(2, 4, "wl2", 0));

        // test-2
        testList2.add(new Result(0, 0, "wl4", 0));
        testList2.add(new Result(0, 2, "wt1", 0));
        testList2.add(new Result(0, 3, "r0", 1000));
        testList2.add(new Result(0, 4, "wt1", 0));
        testList2.add(new Result(0, 6, "wl1", 0));
        testList2.add(new Result(1, 0, "r11", 10));
        testList2.add(new Result(1, 4, "r222", 10));
        testList2.add(new Result(1, 6, "r3", 1000));
        testList2.add(new Result(2, 0, "wt4", 0));
        testList2.add(new Result(2, 1, "r", 10));
        testList2.add(new Result(2, 2, "wt3", 0));
        testList2.add(new Result(2, 4, "wt3", 0));
        testList2.add(new Result(2, 5, "r5", 10));
        testList2.add(new Result(2, 6, "wt2", 0));
        testList2.add(new Result(3, 0, "wl3", 0));
        testList2.add(new Result(3, 1, "v", 20));
        testList2.add(new Result(3, 6, "wl2", 0));

        try {
            // read the data
            TableGenerator TableGenerator = new TableGenerator(4, 7, testList2);

            // get the nodes
            String input = TableGenerator.getOutput();

            // get the tables
            Result[][] table = TableGenerator.getTable();
            int[][] detected = TableGenerator.getDetected();
            int[][] circuitTable = TableGenerator.getCircuitTable();

            showMeTheTables(table, detected, circuitTable);
            System.out.println(input);
            // new ImageGenerator(imageFolderName, table, detected, circuitTable);

            // solve the problem
            CircuitSolver solver = new CircuitSolver(input, timeStep,
                    new PrintStream(new FileOutputStream(outputFileName)));
            solver.run(timeAll);
            // System.out.println(solver.getResponse());

        } catch (Exception e) {
            System.out.println("出现严重故障, 错误信息如下:");
            e.printStackTrace();
        }
    }
}