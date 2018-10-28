package research.sumit0161.entity;

import java.io.Serializable;

public class Container implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2340836264228911330L;
	private int capacity=0;
	private int threshold=0;
	private int currentUsage=0;
	
	public enum ContainerStatus{
		OVERLOADED,FULLUTILIZED,EMPTY,UNDERUTILIZED,OPTIMALUTILIZED
	};
	
	private ContainerStatus status=ContainerStatus.EMPTY;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Container [capacity=" + capacity + ", threshold=" + threshold + ", currentUsage=" + currentUsage
				+ ", status=" + status + "]";
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getCurrentUsage() {
		return currentUsage;
	}

	public boolean setCurrentUsage(int load) {
		if(this.currentUsage==0&&load<=capacity)
		{
				if(load<threshold)
				{
					this.currentUsage = load;
					setStatus(ContainerStatus.UNDERUTILIZED);
					return true;
				}
				else if(load ==threshold)
				{
					this.currentUsage=load;
					setStatus(ContainerStatus.OPTIMALUTILIZED);
					return true;
				}
				else if(load==capacity)
				{
					this.currentUsage=load;
					setStatus(ContainerStatus.FULLUTILIZED);
					return true;
				}
				else
				{
					return false;
				}
		}
		else if(load>capacity||(load+this.currentUsage)>capacity) 
		{
			return false;
		}
		else if((load+this.currentUsage)<capacity)
		{
			int newLoad=this.currentUsage+load;
			if(newLoad<threshold)
			{
				this.currentUsage = newLoad;
				setStatus(ContainerStatus.UNDERUTILIZED);
				return true;
			}
			else if(newLoad ==threshold)
			{
				this.currentUsage=newLoad;
				setStatus(ContainerStatus.OPTIMALUTILIZED);
				return true;
			}
			else if(newLoad==capacity)
			{
				this.currentUsage=newLoad;
				setStatus(ContainerStatus.FULLUTILIZED);
				return true;
			}
			else
			{
				return false;
			}
			
		}
		else
		{
			return false;
		}
		
				
	}
	
	
	public ContainerStatus getStatus() {
		return status;
	}

	public void setStatus(ContainerStatus status) {
		this.status = status;
	} 

	
	
}
