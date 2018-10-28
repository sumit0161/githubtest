package research.sumit0161.entity;

import java.io.Serializable;

public class EdgeConfiguration implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9110043998693594803L;
	private int NumberOfContainers=0;
			   // in sectors
	private int CapacityOfContainer=0; // in custom unit
	private int Threshold=0;				  	
	private boolean isEnergyTypeAC=false;
	public EdgeConfiguration(int numberOfContainers, int radius, int capacityOfContainer, int threshold,
			boolean isEnergyTypeAC) {
		super();
		NumberOfContainers = numberOfContainers;
		
		CapacityOfContainer = capacityOfContainer;
		Threshold = threshold;
		this.isEnergyTypeAC = isEnergyTypeAC;
	}
	public int getNumberOfContainers() {
		return NumberOfContainers;
	}
	public void setNumberOfContainers(int numberOfContainers) {
		NumberOfContainers = numberOfContainers;
	}
	
	public int getCapacityOfContainer() {
		return CapacityOfContainer;
	}
	public void setCapacityOfContainer(int capacityOfContainer) {
		CapacityOfContainer = capacityOfContainer;
	}
	public int getThreshold() {
		return Threshold;
	}
	public void setThreshold(int threshold) {
		Threshold = threshold;
	}
	public boolean isEnergyTypeAC() {
		return isEnergyTypeAC;
	}
	public void setEnergyTypeAC(boolean isEnergyTypeAC) {
		this.isEnergyTypeAC = isEnergyTypeAC;
	}
	@Override
	public String toString() {
		return "EdgeConfiguration [NumberOfContainers=" + NumberOfContainers + ",  CapacityOfContainer=" + CapacityOfContainer + ", Threshold=" + Threshold + ", isEnergyTypeAC="
				+ isEnergyTypeAC + "]";
	}
	
	
	
	
}
