package research.sumit0161.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MobileDevice implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5207427950367718462L;
	private int speed;//IN METER PER SECOND
	private int bandwidth;// IN KB PER SECOND
	private int energyRequired;// IN 20 watt to 80 watt
	private int cpu; // in 20 to 100 cpu
	
	private Location startLocation;
	private int currentManeuver=0;
	private Location currentLocation;
	private Location targetLocation;
	public int totalDistance;
	public ArrayList<Maneuver> navigation= new ArrayList<Maneuver>();
	private ArrayList<Location> speedBasedEverySecondLocation= new ArrayList<Location>();
	public ArrayList<Location> getSpeedBasedEverySecondLocation() {
		return speedBasedEverySecondLocation;
	}

	public void addSpeedBasedEverySecondLocation(Location speedBasedEverySecondLocation) {
		this.speedBasedEverySecondLocation.add(speedBasedEverySecondLocation);
	}

	public Maneuver[] getNavigation() {
		
		if(currentManeuver+1<navigation.size())
		{
		Maneuver [] maneuvers= new Maneuver[2];
		
		maneuvers[0]= navigation.get(currentManeuver);
		currentManeuver++;
		maneuvers[1]= navigation.get(currentManeuver);
		
		return maneuvers;
		}
		else
		{
		return null;
		}
	}

	public void addNavigation(Maneuver object) {
		this.navigation.add(object);
	}
	public void setSpeedBasedEverySecondLocation(ArrayList<Location> allLocations) {
		this.speedBasedEverySecondLocation=allLocations;
	}
	
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}

	

	public EdgeServer getServingEdgeServer() {
		return servingEdgeServer;
	}

	public void setServingEdgeServer(EdgeServer servingEdgeServer) {
		this.servingEdgeServer = servingEdgeServer;
	}

	private EdgeServer servingEdgeServer;



	@Override
	public String toString() {
		return "MobileDevice [speed=" + speed + ", bandwidth=" + bandwidth + ", energyRequired=" + energyRequired
				+ ", cpu=" + cpu + ", startLocation=" + startLocation + ", currentManeuver=" + currentManeuver
				+ ", currentLocation=" + currentLocation + ", targetLocation=" + targetLocation + ", totalDistance="
				+ totalDistance + ", navigation=" + navigation + ", speedBasedEverySecondLocation="
				+ speedBasedEverySecondLocation + ", servingEdgeServer=" + servingEdgeServer + "]";
	}

	public int getEnergyRequired() {
		return energyRequired;
	}

	public void setEnergyRequired(int energyRequired) {
		this.energyRequired = energyRequired;
	}

	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	
	
}
