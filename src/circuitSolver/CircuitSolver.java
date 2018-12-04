package circuitSolver;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * ��·���������. ��Ҫʹ���˸Ľ��ڵ㷨
 */
public class CircuitSolver {

    private ArrayList<Node> nodes; // �ڵ��б�
    private ArrayList<Branch> branches; // ֧·�б�

    // �ļ����������������

    private FileProcessor processor;
    private Scanner scanner;
    private String input;
    private PrintStream output;

    private double timeStep;

    private String response = new String();

    public String getResponse() {
        return response;
    }

    public CircuitSolver(String input, double timeStep, PrintStream output) {
        this.timeStep = timeStep;
        this.input = input;
        this.output = output;

        initialize(); // Ԥ����, �Թ���Nodes��Branches��
    }

    /**
     * 1. ������·Ԫ������ 2. ���ýڵ��֧·���� 3. ���ýӵؽڵ�
     */
    public void initialize() {

        // �½�����
        nodes = new ArrayList<Node>();
        branches = new ArrayList<Branch>();
        processor = new FileProcessor(this);

        // ��ȡ�ļ��ṩ�Ľڵ���Ϣ
        int nodeCount = 0;
        try {
            scanner = new Scanner(input);

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

        // ���ӵؽڵ����ڵ���
        nodeCount++;

        // ���������ڵ�
        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new Node(i));
        }

        // ��ȡ�ļ��ṩ��֧·��Ϣ
        try {
            scanner = new Scanner(input);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                processor.processLine(line);
            }
        } catch (Exception e) {
            output.println("Error_3...");
        }
        Collections.sort(nodes);

        // ������Ҫŵ�ٵ�Ч��֧·
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

        // ����DC��·���
        DCSolve();
    }

    public void DCSolve() {
        // ��������
        Matrix A = new Matrix(nodes.size(), nodes.size());
        Matrix b = new Matrix(nodes.size(), 1);
        LinearSolver solver = new LinearSolver();

        // ŵ�ٵ�Чʹ��ѹԴת��Ϊ����͵���Դ�ĵ�Ч
        for (Branch branch : branches) {
            int anodeIndex = branch.getAnode().getIndex();
            int cathodeIndex = branch.getCathode().getIndex();
            // �������֧·����Դ
            if (branch instanceof CurrentSource) {
                b.setValue(b.getValue(anodeIndex, 0) + branch.getValue(), anodeIndex, 0);
                b.setValue(b.getValue(cathodeIndex, 0) - branch.getValue(), cathodeIndex, 0);
            } else {
                // ��������ڵ���
                A.setValue(A.getValue(anodeIndex, cathodeIndex) - branch.getConductance(), anodeIndex, cathodeIndex);
                A.setValue(A.getValue(cathodeIndex, anodeIndex) - branch.getConductance(), cathodeIndex, anodeIndex);
                A.setValue(A.getValue(anodeIndex, anodeIndex) + branch.getConductance(), anodeIndex, anodeIndex);
                A.setValue(A.getValue(cathodeIndex, cathodeIndex) + branch.getConductance(), cathodeIndex,
                        cathodeIndex);
            }
        }

        // �Ƴ��ӵصľ�������
        A = A.removeRow(0);
        A = A.removeColumn(0);
        b = b.removeRow(0);

        Matrix solution = solver.gaussianElimination(A, b);

        // ���ýӵ����
        for (int i = 0; i < solution.countRows(); i++) {
            nodes.get(i + 1).setVoltage(solution.getValue(i, 0));
        }

        for (Branch branch : branches) {
            branch.update();
        }
    }

    /**
     * ��ʱ�������г������
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
     * ���������������
     */
    public void print(double time) {
        String response = String.format("Time:\t" + "%.02f", time) + "\t\t\n" + "Id\t\tValue\t\tVoltage\t\tCurrent";
        for (Branch branch : branches) {
            if (!branch.isCompanion()) {
                response += "\n" + branch.toString() + "\t\t\t";
            }
        }

        output.println(response);
        this.response += response + "\n"; // ʱ��
        // this.response = response; // ����
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