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

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Result> list = new ArrayList<>();
        list.add(new Result(0, 0, "wl4", 0));
        list.add(new Result(0, 2, "v0", 10));
        list.add(new Result(0, 4, "wl1", 0));
        list.add(new Result(1, 0, "wt4", 0));
        list.add(new Result(1, 1, "r0", 2));
        list.add(new Result(1, 3, "r2", 2));
        list.add(new Result(1, 4, "wt2", 0));
        list.add(new Result(2, 0, "wl3", 0));
        list.add(new Result(2, 1, "r1", 20));
        list.add(new Result(2, 3, "r3", 20));
        list.add(new Result(2, 4, "wl2", 0));

        try {
            TableGenerator TableGenerator = new TableGenerator(3, 5, list);
            String input = TableGenerator.getOutput();
            Result[][] table = TableGenerator.getTable();
            int[][] detected = TableGenerator.getDetected();

            System.out.println("The original matrix is as below:");
            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[0].length; j++) {
                    System.out.print(table[i][j].getName() + "\t");
                }
                System.out.println();
            }

            System.out.println("The node matrix is as below:");
            for (int i = 0; i < detected.length; i++) {
                for (int j = 0; j < detected[0].length; j++) {
                    System.out.print(detected[i][j] + "\t");
                }
                System.out.println();
            }

            CircuitSolver solver = new CircuitSolver(input, timeStep,
                    new PrintStream(new FileOutputStream(outputFileName)));

            new ImageGenerator(imageFolderName, table, detected);

            solver.run(timeAll);
            // System.out.println(solver.getResponse());

        } catch (Exception e) {
            System.out.println("出现严重故障, 错误信息如下:");
            e.printStackTrace();
        }
    }
}