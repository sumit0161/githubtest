package research.sumit0161.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import research.sumit0161.common.CommonFunctions;
import research.sumit0161.entity.Container.ContainerStatus;

public class EdgeServer implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -201655062377408882L;
	private float CPUUsage= -7.0f;
	private float CPUCapacity=0.0f;
	private EdgeConfiguration config;
	//storage(128-512mb), bandwidth(10-100mb/s),energy(150idle-260),
	
	private int energy=150;
	private int bandwidth=0;
	private int storage=0;
	
	
	// list up all the containers in different categories so that we can assign various jobs easily later on
	
	private ArrayList<Container> idleContainers= new ArrayList<Container>();
	private ArrayList<Container> overloadedContainers= new ArrayList<Container>();
	private ArrayList<Container> underloadedContainers= new ArrayList<Container>();
	private ArrayList<Container> allContainers= new ArrayList<Container>();
	
	
	private Location serverLocation;

	
	
	
	
	public ArrayList<Container> getIdleContainers() {
		return idleContainers;
	}

	public void setIdleContainers(ArrayList<Container> idleContainers) {
		this.idleContainers = idleContainers;
	}

	public ArrayList<Container> getOverloadedContainers() {
		return overloadedContainers;
	}

	public void setOverloadedContainers(ArrayList<Container> overloadedContainers) {
		this.overloadedContainers = overloadedContainers;
	}

	public ArrayList<Container> getUnderloadedContainers() {
		return underloadedContainers;
	}

	public void setUnderloadedContainers(ArrayList<Container> underloadedContainers) {
		this.underloadedContainers = underloadedContainers;
	}

	public ArrayList<Container> getAllContainers() {
		return allContainers;
	}

	public void setAllContainers(ArrayList<Container> allContainers) {
		this.allContainers = allContainers;
		repopulateContainerLists();
	}

	
	public EdgeConfiguration getConfig() {
		return config;
	}

	public void repopulateContainerLists()
	{
		idleContainers.clear();
		overloadedContainers.clear();
		underloadedContainers.clear();
		allContainers.stream().forEach(arg0->{
			ContainerStatus status=arg0.getStatus();
			
			if(status==ContainerStatus.EMPTY)
			{
				idleContainers.add(arg0);
			}else if(status==ContainerStatus.UNDERUTILIZED)
			{
				underloadedContainers.add(arg0);
			}else if(status==ContainerStatus.OPTIMALUTILIZED||status==ContainerStatus.FULLUTILIZED||status==ContainerStatus.OVERLOADED)
			{
				overloadedContainers.add(arg0);
			}
		});
	}
	
	
	
	
	public void setConfig(EdgeConfiguration config) {
		this.config = config;
		this.CPUCapacity=config.getCapacityOfContainer()*config.getNumberOfContainers();
		this.bandwidth=CommonFunctions.randomValueBetweenMinAndMax(10, 100);
		this.energy=CommonFunctions.randomValueBetweenMinAndMax(150, 260);
		this.storage=CommonFunctions.randomValueBetweenMinAndMax(128, 512);
		for(int i=0;i<config.getNumberOfContainers();i++)
		{
			Container c=new Container();
			c.setCapacity(config.getCapacityOfContainer());
			c.setThreshold(config.getThreshold());
			allContainers.add(c);
		}
		
	}

	public float getCPUUsage() {
		return CPUUsage;
	}

	public boolean setCPUUsage(float cPUUsage) {
		if(cPUUsage>CPUCapacity || cPUUsage<0)
		{
			return false;
		}
		else
		{
			CPUUsage = cPUUsage;
			return true;
		}
	}

	public float getCPUCapacity() {
		return CPUCapacity;
	}

	public void setCPUCapacity(float cPUCapacity) {
		CPUCapacity = cPUCapacity;
	}

	

	public Location getServerLocation() {
		return serverLocation;
	}

	public void setServerLocation(Location serverLocation) {
		this.serverLocation = serverLocation;
	}

	

	@Override
	public String toString() {
		return "EdgeServer [CPUUsage=" + CPUUsage + ", CPUCapacity=" + CPUCapacity + ", energy="
				+ energy + ", bandwidth=" + bandwidth + ", storage=" + storage + ", idleContainers=" + idleContainers
				+ ", overloadedContainers=" + overloadedContainers + ", underloadedContainers=" + underloadedContainers
				+ ", allContainers=" + allContainers + ", serverLocation=" + serverLocation + "]";
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}



}
