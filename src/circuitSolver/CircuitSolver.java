package circuitSolver;

import Jama.*;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * 电路求解主程序. 主要使用了改进节点法
 */
public class CircuitSolver {

    private ArrayList<Node> nodes; // 节点列表
    private ArrayList<Branch> branches; // 支路列表

    // 文件处理与输入输出流

    private FileProcessor processor;
    private String inputFileName;
    private Scanner scanner;
    private PrintStream output;

    private double timeStep;

    private String response = new String();

    public String getResponse() {
        return response;
    }

    public CircuitSolver(String inputFileName, double timeStep, PrintStream output) {
        this.inputFileName = inputFileName;
        this.timeStep = timeStep;
        this.output = output;

        initialize(); // 预处理, 以构造Nodes和Branches类
    }

    /**
     * 1. 创建电路元件对象 2. 设置节点和支路对象 3. 设置接地节点
     */
    public void initialize() {

        // 新建对象
        nodes = new ArrayList<Node>();
        branches = new ArrayList<Branch>();
        processor = new FileProcessor(this);

        // 读取文件提供的节点信息
        int nodeCount = 0;
        try {
            scanner = new Scanner(new File(inputFileName));

            while (scanner.hasNextLine()) {
                try {
                    String line = scanner.nextLine();
                    String[] divided = line.split("\t");
                    int leftValue = Integer.parseInt(divided[2]);
                    int rightValue = Integer.parseInt(divided[3]);

                    if (leftValue >= nodeCount || rightValue >= nodeCount) {
                        nodeCount = (leftValue > rightValue) ? leftValue : rightValue;
                    }
                } catch (Exception e) {
                    output.println("Error_1...");
                }
            }
        } catch (Exception e) {
            output.println("Error_2...");
        }

        // 将接地节点加入节点数
        nodeCount++;

        // 遍历创建节点
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node(i));
        }

        // 读取文件提供的支路信息
        try {
            scanner = new Scanner(new File(inputFileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                processor.processLine(line);
            }
        } catch (Exception e) {
            output.println("Error_3...");
        }
        Collections.sort(nodes);

        // deal with branches that need norton equivalents
        ArrayList<Branch> addedBranches = new ArrayList<Branch>();
        for (Branch branch : branches) {
            if (!(branch instanceof Resistor || branch instanceof CurrentSource)) {
                for (Branch newBranch : branch.createCompanions(this)) {
                    addedBranches.add(newBranch);
                }
            }
        }

        for (Branch branch : addedBranches) {
            branches.add(branch);
        }

        // 启动DC电路求解
        DCSolve();
    }

    public void DCSolve() {
        // 创建矩阵
        Matrix A = new Matrix(nodes.size(), nodes.size());
        Matrix b = new Matrix(nodes.size(), 1);

        // 诺顿等效使电压源转化为电阻和电流源的等效
        for (Branch branch : branches) {
            int anodeIndex = branch.getAnode().getIndex();
            int cathodeIndex = branch.getCathode().getIndex();

            // 如果存在支路电流源
            if (branch instanceof CurrentSource) {
                b.set(anodeIndex, 0, b.get(anodeIndex, 0) + branch.getValue());
                b.set(cathodeIndex, 0, b.get(cathodeIndex, 0) - branch.getValue());
            } else {
                // 如果仅存在电阻
                A.set(anodeIndex, cathodeIndex, A.get(anodeIndex, cathodeIndex) - branch.getConductance());
                A.set(cathodeIndex, anodeIndex, A.get(cathodeIndex, anodeIndex) - branch.getConductance());
                A.set(anodeIndex, anodeIndex, A.get(anodeIndex, anodeIndex) + branch.getConductance());
                A.set(cathodeIndex, cathodeIndex, A.get(cathodeIndex, cathodeIndex) + branch.getConductance());
            }
        }

        // 移除接地的矩阵序列
        A = A.getMatrix(1, A.getRowDimension() - 1, 1, A.getColumnDimension() - 1);
        b = b.getMatrix(1, b.getRowDimension() - 1, 0, b.getColumnDimension() - 1);

        Matrix solution = A.solve(b);

        // 设置接地零点
        for (int i = 0; i < solution.getRowDimension(); i++) {
            nodes.get(i + 1).setVoltage(solution.get(i, 0));
        }

        for (Branch branch : branches) {
            branch.update();
        }
    }

    /**
     * 以时间跨度运行程序迭代
     */
    public void run(double timeAll) {
        double time = 0;
        while (time <= timeAll) {
            print(time);
            for (Branch branch : branches) {
                branch.update();
            }
            DCSolve();
            time += timeStep;
        }
    }

    /**
     * 输出迭代的运算结果
     */
    public void print(double time) {
        String response = String.format("Time:\t" + "%.02f", time) + "\t\t\n" + "Id\t\tValue\t\tVoltage\t\tCurrent";
        for (Branch branch : branches) {
            if (!branch.isCompanion()) {
                response += "\n" + branch.toString() + "\t\t\t";
            }
        }

        output.println(response);
        this.response += response + "\n"; // 时域
        // this.response = response; // 单独
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public Node getWithIndex(int index) {
        for (Node node : nodes) {
            if (node.getIndex() == index) {
                return node;
            }
        }
        return null;
    }

    public boolean containsWithIndex(int index) {
        boolean response = false;
        for (Node node : nodes) {
            if (node.getIndex() == index) {
                return true;
            }
        }
        return response;
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    public void setBranches(ArrayList<Branch> branches) {
        this.branches = branches;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public PrintStream getOutput() {
        return output;
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }
}