package circuitSolver;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * ��·֧·������, ����Ԫ���ཫ������һ��.
 */
abstract class Branch {

    private String id;
    private double value;
    private Node cathode;
    private Node anode;
    private boolean isCompanion;

    private double voltage; // �洢֧·��ѹ
    private double current; // �洢֧·����
    private double conductance; // �洢֧·�迹

    private DecimalFormat form = new DecimalFormat("#.##"); // �����ʽ�淶ΪС�������λ

    public Branch(String id, double value) {
        this.id = id;
        this.value = value;
        this.isCompanion = false;
    }

    /**
     * �����迹, ��ѹ, ������ϵ
     */
    abstract protected void update();

    /**
     * ��ķ�װ
     */

    protected void setConductance(double conductance) {
        this.conductance = conductance;
    }

    protected void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    protected void setCurrent(double current) {
        this.current = current;
    }

    protected String getId() {
        return id;
    }

    protected double getValue() {
        return value;
    }

    protected void setValue(double newValue) {
        this.value = newValue;
    }

    protected Node getCathode() {
        return cathode;
    }

    protected Node getAnode() {
        return anode;
    }

    protected void setCathode(Node cathode) {
        this.cathode = cathode;
    }

    protected void setAnode(Node anode) {
        this.anode = anode;
    }

    protected boolean isCompanion() {
        return isCompanion;
    }

    protected void setCompanion(boolean bool) {
        this.isCompanion = bool;
    }

    protected ArrayList<Branch> createCompanions(CircuitSolver solver) {
        return new ArrayList<Branch>();
    }

    /**
     * ȡ����ѹ, ����, �迹ֵ
     */
    protected double getVoltage() {
        return voltage;
    }

    protected double getCurrent() {
        return current;
    }

    protected double getConductance() {
        return conductance;
    }

    public String toString() {
        return id + "\t\t" + form.format(value) + "\t\t" + form.format(getVoltage()) + "\t\t"
                + form.format(getCurrent());
    }
}