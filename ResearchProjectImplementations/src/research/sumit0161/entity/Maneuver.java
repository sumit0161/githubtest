package research.sumit0161.entity;

import java.io.Serializable;

public class Maneuver implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8027583785471123176L;
	private Location current;
	private String instruction;
	private int travelTime;
	private int distance;
	
	
	
	
	@Override
	public String toString() {
		return "Maneuver [current=" + current + ", instruction=" + instruction + ", travelTime=" + travelTime
				+ ", distance=" + distance + "]";
	}
	public Location getCurrent() {
		return current;
	}
	public void setCurrent(Location current) {
		this.current = current;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public int getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if(((Maneuver)arg0).equals(current))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
}
