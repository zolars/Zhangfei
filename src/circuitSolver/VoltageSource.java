package circuitSolver;

import java.util.ArrayList;

public class VoltageSource extends Branch {

    private CurrentSource Ieq;
    private Resistor Req;
    private double internalResistance;

    public VoltageSource(String id, double voltage) {
        super(id, voltage);
        // 设置小数单位
        internalResistance = 1E-10;
    }

    protected ArrayList<Branch> createCompanions(CircuitSolver solver) {

        Ieq = new CurrentSource(getId() + "_Ieq", getValue() / internalResistance);
        Req = new Resistor(getId() + "_Req", internalResistance);

        Req.setAnode(getAnode());
        Req.setCathode(getCathode());

        Ieq.setAnode(getAnode());
        Ieq.setCathode(getCathode());

        Ieq.setCompanion(true);
        Req.setCompanion(true);

        // add to nodes and engine branch list
        getAnode().addBranch(Ieq);
        getCathode().addBranch(Req);

        getAnode().removeComponent(this);
        getCathode().removeComponent(this);

        ArrayList<Branch> newBranches = new ArrayList<Branch>();
        newBranches.add(Ieq);
        newBranches.add(Req);
        return newBranches;
    }

    protected void setinternalResistance(double internalResistance) {
        this.internalResistance = internalResistance;
    }

    @Override
    protected void setValue(double newValue) {
        Ieq.setValue(newValue / internalResistance);
    }

    protected void setCompanionAnodes(Node node) {
        Ieq.setAnode(node);
        Req.setAnode(node);
    }

    protected void setCompanionCathodes(Node node) {
        Ieq.setCathode(node);
        Req.setCathode(node);
    }

    protected void setCompanionBoolean(Boolean bool) {
        Ieq.setCompanion(bool);
        Req.setCompanion(bool);
    }

    @Override
    protected void update() {
        // constant voltage source
    }
}