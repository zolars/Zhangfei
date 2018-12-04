package circuitSolver;

public class CurrentSource extends Branch{
	
	public CurrentSource(String id, double current){
		super(id, current);
		setCurrent(current);
	}

	@Override
	protected void update() {
		// 无需更改
	}
}