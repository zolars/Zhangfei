package circuitSolver;

import java.util.ArrayList;

/**
 * Uses Norton Equivalent for the capacitor: A resistor in parallel with a
 * current source
 *
 * @author Alan Yang
 */
public class Capacitor extends Branch {

    private Resistor Req;
    private CurrentSource Ieq;
    private double timeStep;

    public Capacitor(String id, double capacitance, double timeStep) {
        super(id, capacitance);
        this.timeStep = timeStep;
        setConductance(capacitance / timeStep);
    }

    protected ArrayList<Branch> createCompanions(CircuitSolver solver) {
        Req = new Resistor(getId() + "_Req", 1 / getConductance());
        Ieq = new CurrentSource(getId() + "_Ieq", getConductance() * getVoltage());

        Req.setAnode(getAnode());
        Req.setCathode(getCathode());

        Ieq.setAnode(getAnode());
        Ieq.setCathode(getCathode());

        Ieq.setCompanion(true);
        Req.setCompanion(true);

        // add to nodes and solver branch list
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
        // ensure correct Ieq polarity
        /**
         int factor = 1;
         double anodeV = getAnode().getVoltage();
         double cathodeV = getCathode().getVoltage();
         if (getAnode().getVoltage() < getCathode().getVoltage()){
         double swapped = anodeV;
         anodeV = cathodeV;
         cathodeV = swapped;
         factor = -1;
         }
         */
        setVoltage(getAnode().getVoltage() - getCathode().getVoltage());
        setCurrent(getAnode().getCurrentInto(this));
        Ieq.setValue((getVoltage() * getConductance()));
    }
}