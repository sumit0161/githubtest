package research.sumit0161.common;

import java.util.ArrayList;

import research.sumit0161.entity.EdgeConfiguration;
import research.sumit0161.entity.EdgeServer;
import research.sumit0161.entity.Location;
import research.sumit0161.entity.MobileDevice;

public class CommonVariables {

	//Edge Configuration params are no. of containers,radius of coverage of edge,capacity,threshold,isAcEnergy//
	public static EdgeConfiguration FirstConfiguration= new EdgeConfiguration(4,4,256,230,true);
	public static EdgeConfiguration SecondConfiguration= new EdgeConfiguration(6,4,512,450,true);
	public static EdgeConfiguration ThirdConfiguration= new EdgeConfiguration(3,2,256,200,false);
	public static EdgeConfiguration FourthConfiguration= new EdgeConfiguration(5,3,400,370,false);
	
	public static final String app_id="4uaRTQHpqlbK7GfR7RJk";
	public static final String app_code="euv38POmKa7_UPoJaiSZKw";		
	
	public static ArrayList<EdgeServer>servers= new ArrayList<EdgeServer>();
	public static ArrayList<Location> possibleMobileDevices= new ArrayList<Location>();
	public static ArrayList<MobileDevice> mobileDevices= new ArrayList<MobileDevice>();
	
	
	
	
	
	
}
