package circuitSolver;

import java.util.*;
public class Node implements Comparable <Node> {
	private ArrayList<Branch> connections;
	private double voltage;
	private int index;
	
	public Node(int index){
		connections = new ArrayList<Branch>();
		this.index = index;
	}

	public double getCurrentInto(Branch branch){
		double response = 0;
		for (Branch br : getConnections()){
			if (!br.getId().equals(branch.getId())){
				response += br.getCurrent();
			}
		}
		return response;
	}
	
	public double getCurrentOutOf(Branch branch){
		double response = 0;
		for (Branch br : getConnections()){
			if (br.getId().equals(branch.getId())){
				response += br.getCurrent();
			}
		}
		return response;
	}
	
	public int getIndex() {
		return index;
	}

	public ArrayList<Branch> getConnections(){
		return connections;
	}
	
	public void addBranch(Branch newComponent){
		connections.add(newComponent);
	}
	
	public void removeComponent(Branch removed){
		if (connections.contains(removed)){
			connections.remove(removed);
		}
	}

	public double getVoltage(){
		return voltage;
	}
	
	public void setVoltage(double voltage){
		this.voltage = voltage;
	}

	@Override
	public int compareTo(Node thatNode) {
		int thatIndex = thatNode.getIndex();
		if (this.getIndex() < thatIndex) {
			return -1;
		} else if (this.getIndex() > thatIndex) {
			return 1;
		} else {
			return 0;
		}
	}
}