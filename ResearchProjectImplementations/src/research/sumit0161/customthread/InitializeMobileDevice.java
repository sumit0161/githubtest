package research.sumit0161.customthread;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import research.sumit0161.common.CommonFunctions;
import research.sumit0161.common.CommonVariables;
import research.sumit0161.entity.Location;
import research.sumit0161.entity.MobileDevice;

public class InitializeMobileDevice implements Runnable{

	private Location startLocation;
	private Location endLocation;
	
	
	
	public InitializeMobileDevice(Location startLocation, Location endLocation) {
		super();
		this.startLocation = startLocation;
		this.endLocation = endLocation;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			MobileDevice objectMobileDevice=CommonFunctions.constructMobileDevice(startLocation, endLocation);
			System.out.println(objectMobileDevice);
			CommonVariables.mobileDevices.add(objectMobileDevice);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
