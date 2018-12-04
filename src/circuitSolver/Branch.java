package circuitSolver;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 电路支路抽象类, 其余元件类将补完这一类.
 */
abstract class Branch {

    private String id;
    private double value;
    private Node cathode;
    private Node anode;
    private boolean isCompanion;

    private double voltage; // 存储支路电压
    private double current; // 存储支路电流
    private double conductance; // 存储支路阻抗

    private DecimalFormat form = new DecimalFormat("#.##"); // 输出格式规范为小数点后三位

    public Branch(String id, double value) {
        this.id = id;
        this.value = value;
        this.isCompanion = false;
    }

    /**
     * 更新阻抗, 电压, 电流关系
     */
    abstract protected void update();

    /**
     * 类的封装
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
     * 取出电压, 电流, 阻抗值
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