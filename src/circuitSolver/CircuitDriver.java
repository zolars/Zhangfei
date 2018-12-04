package circuitSolver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class CircuitDriver {
    private static final String folderName = "C:\\Users\\DELL\\Desktop";
    private static final String inputFileName = folderName + "\\CircuitInput.txt";
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
        list.add(new Result(1, 3, "r2", 20));
        list.add(new Result(1, 4, "wt2", 0));
        list.add(new Result(2, 0, "wl3", 0));
        list.add(new Result(2, 1, "r1", 20));
        list.add(new Result(2, 3, "r3", 20));
        list.add(new Result(2, 4, "wl2", 0));
        try {
            new TableChange(3, 5, list, inputFileName);
        } catch (Exception e) {
            System.out.println("ʶ��ת�����̳������ع���, ������Ϣ����:");
            e.printStackTrace();
        }

        CircuitSolver solver = new CircuitSolver(inputFileName, timeStep,
                new PrintStream(new FileOutputStream(outputFileName)));
        solver.run(timeAll);
        String response = solver.getResponse();
        System.out.println(response);
    }
}