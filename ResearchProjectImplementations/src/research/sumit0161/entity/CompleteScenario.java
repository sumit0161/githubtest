package research.sumit0161.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class CompleteScenario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4546117504758778654L;
	private float[][][] distances;
	
	private  ArrayList<EdgeServer>servers= new ArrayList<EdgeServer>();
	
	private  ArrayList<MobileDevice> mobileDevices= new ArrayList<MobileDevice>();

	@Override
	public String toString() {
		return "CompleteScenario  ---->[servers=" + servers + ", mobileDevices=" + mobileDevices + "]";
	}

	public ArrayList<EdgeServer> getServers() {
		return servers;
	}

	public void setServers(ArrayList<EdgeServer> servers) {
		this.servers = servers;
	}

	public ArrayList<MobileDevice> getMobileDevices() {
		return mobileDevices;
	}

	public void setMobileDevices(ArrayList<MobileDevice> mobileDevices) {
		this.mobileDevices = mobileDevices;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public float[][][] getDistances() {
		return distances;
	}

	public void setDistances(float[][][] distances) {
		this.distances = distances;
	}
	
	
	
}
