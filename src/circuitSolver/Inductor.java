package circuitSolver;

import java.util.ArrayList;

public class Inductor extends Branch {

    private Resistor Req;
    private CurrentSource Ieq;
    private double timeStep;

    public Inductor(String id, double inductance, double timeStep) {
        super(id, inductance);
        this.timeStep = timeStep;
        setConductance(timeStep / inductance);
    }

    protected ArrayList<Branch> createCompanions(CircuitSolver solver) {
        Req = new Resistor(getId() + "_Req", 1 / getConductance());
        Ieq = new CurrentSource(getId() + "_Ieq", getConductance() * getVoltage());

        Req.setAnode(getAnode());
        Req.setCathode(getCathode());

        Ieq.setAnode(getCathode());
        Ieq.setCathode(getAnode());

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

    @Override
    protected void setValue(double newValue) {
        setConductance(newValue / timeStep);
    }

    @Override
    protected void update() {
        int factor = -1;
        double anodeV = getAnode().getVoltage();
        double cathodeV = getCathode().getVoltage();
        if (getAnode().getVoltage() < getCathode().getVoltage()) {
            double swapped = anodeV;
            anodeV = cathodeV;
            cathodeV = swapped;
            factor = 1;
        }

        setVoltage(anodeV - cathodeV);
        setCurrent(getConductance() * getVoltage());
        Ieq.setValue(factor * (getVoltage() * getConductance() + getCurrent()));
    }
}