package research.sumit0161.entity;

import java.io.Serializable;

public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1017795290907130473L;
	private double lati=-1;
	private double longi=-1;
	public double getLati() {
		return lati;
	}
	public Location(double lati, double longi) {
		super();
		this.lati = lati;
		this.longi = longi;
	}
	public void setLati(double lati) {
		this.lati = lati;
	}
	public double getLongi() {
		return longi;
	}
	public void setLongi(double longi) {
		this.longi = longi;
	}
	@Override
	public String toString() {
		return "Location [lati=" + lati + ", longi=" + longi + "]";
	}
	
	
	
	
}
