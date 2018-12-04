package circuitSolver;

public class Resistor extends Branch {

    public Resistor(String id, double resistance) {
        super(id, resistance);
        setConductance(1 / resistance);
    }

    @Override
    protected void update() {
        // ʱ�����
        if (getCathode().getIndex() == 0) {
            setVoltage(getAnode().getVoltage());
        } else if (getAnode().getIndex() == 0) {
            setVoltage(getCathode().getVoltage());
        } else if (getAnode().getIndex() < getCathode().getIndex()) {
            setVoltage(getAnode().getVoltage() - getCathode().getVoltage());
        } else {
            setVoltage(-getAnode().getVoltage() + getCathode().getVoltage());
        }
        setCurrent(getVoltage() * getConductance());
    }
}