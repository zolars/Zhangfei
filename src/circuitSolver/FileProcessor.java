package circuitSolver;

/**
 * 文件处理类
 */
public class FileProcessor {

	private static final char VoltageSource = 'v';
	private static final char CurrentSource = 'i';
	private static final char Resistor = 'r';
	private static final char Capacitor = 'c';
	private static final char Inductor = 'l';

	private double timeStep;
	private CircuitSolver solver;

	public FileProcessor(CircuitSolver solver) {
		this.solver = solver;
		this.timeStep = solver.getTimeStep();
	}

	public void processLine(String line) {
		String[] divided = line.split("\t");
		Branch branch = makeBranch(divided[0], divided[1], timeStep);

		int anodeIndex = Integer.parseInt(divided[2]);
		int cathodeIndex = Integer.parseInt(divided[3]);

		solver.getWithIndex(anodeIndex).addBranch(branch);
		branch.setAnode(solver.getWithIndex(anodeIndex));
		solver.getWithIndex(cathodeIndex).addBranch(branch);
		branch.setCathode(solver.getWithIndex(cathodeIndex));

		solver.getBranches().add(branch);
	}

	public Branch makeBranch(String id, String val, double timeStep) {
		double value = Double.parseDouble(val);
		Branch branch = null;

		switch (id.charAt(0)) {
		case (VoltageSource):
			branch = new VoltageSource(id, value);
			break;
		case (CurrentSource):
			branch = new CurrentSource(id, value);
			break;
		case (Resistor):
			branch = new Resistor(id, value);
			break;
		case (Capacitor):
			branch = new Capacitor(id, value, timeStep);
			break;
		case (Inductor):
			branch = new Inductor(id, value, timeStep);
			break;
		}
		return branch;
	}
}