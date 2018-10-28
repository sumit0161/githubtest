package research.sumit0161.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import research.sumit0161.entity.Location;
import research.sumit0161.entity.Maneuver;
import research.sumit0161.entity.MobileDevice;

public class CommonFunctions {

	public static MobileDevice constructMobileDevice(Location startLocation, Location endLocation) throws IOException, ParseException
	{
		
		
		String startingPoint="geo!"+startLocation.getLati()+","+startLocation.getLongi();
		String endingPoint="geo!"+endLocation.getLati()+","+endLocation.getLongi();
		String url = "https://route.api.here.com/routing/7.2/calculateroute.json?app_id="+CommonVariables.app_id+"&app_code="+CommonVariables.app_code+"&waypoint0="+startingPoint+"&waypoint1="+endingPoint+"&mode=fastest;car;traffic:disabled";
        String inputLine;
		String exploreJSONCall = url;
		URL myURL = new URL(exploreJSONCall);
		HttpURLConnection yc = (HttpURLConnection) myURL.openConnection();
		yc.setRequestMethod("GET");
		yc.setRequestProperty("User-Agent", "Mozilla/5.0");
		yc.setRequestProperty("followAllRedirects", "true");
		yc.setRequestProperty("authority", "route.api.here.com");
		yc.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		inputLine=in.readLine();
		MobileDevice objectMobileDevice=new MobileDevice();
		Random objectR= new Random();
		objectMobileDevice.setBandwidth(objectR.nextInt(2048));
		int randomValue=objectR.nextInt(100);
		if(randomValue<50) 
		{
			objectMobileDevice.setSpeed(20);
		}
		else
		{
			objectMobileDevice.setSpeed(10);
		}
		objectMobileDevice.setCpu(CommonFunctions.randomValueBetweenMinAndMax(20, 100));
		objectMobileDevice.setEnergyRequired(CommonFunctions.randomValueBetweenMinAndMax(20, 80));
		objectMobileDevice.setCurrentLocation(startLocation);
		objectMobileDevice.setStartLocation(startLocation);
		objectMobileDevice.setTargetLocation(endLocation);

		JSONParser object= new JSONParser();	
		Object obj = object.parse(inputLine);
        JSONObject jo = (JSONObject)obj;
        JSONObject response=(JSONObject)jo.get("response");
        JSONArray route=(JSONArray)response.get("route");
        JSONObject zero=(JSONObject)route.get(0);
        JSONObject summary=(JSONObject)zero.get("summary");
        objectMobileDevice.totalDistance=Integer.parseInt(summary.get("distance")+"");	
        
        JSONArray leg=(JSONArray)zero.get("leg");
        JSONObject zero2=(JSONObject)leg.get(0);
        JSONArray maneuver=(JSONArray)zero2.get("maneuver");
        for(int i=0;i<maneuver.size()-1;i++)
        {
        Maneuver objectManeuver= new Maneuver();
        JSONObject zero3=(JSONObject)maneuver.get(i);
        JSONObject currentLatLong=(JSONObject)zero3.get("position");
        objectManeuver.setCurrent(new Location(Double.parseDouble(""+currentLatLong.get("latitude")),Double.parseDouble(""+currentLatLong.get("longitude"))));
        objectManeuver.setDistance(Integer.parseInt(""+zero3.get("length")));
        objectManeuver.setInstruction(""+zero3.get("instruction"));
        objectManeuver.setTravelTime(Integer.parseInt(""+zero3.get("travelTime")));
        objectMobileDevice.addNavigation(objectManeuver);
        }
		return objectMobileDevice;
	}
	

	public static int randomValueBetweenMinAndMax(int min, int max)
	{
		Random objectRandom= new Random();
		int newMax=max-min;
		int value=objectRandom.nextInt(newMax);
		value=min/2+value;
		while(value<min)
		{
			value=min/4+value;
		}
		return value;
	}
	
	
	public static ArrayList<Location> getLocations(int interval, double azimuth, Location start, Location end) 
	{
        double d = getPathLength(start, end);
        int dist = (int) d / interval;
        int coveredDist = interval;
        int counter=-1;
        ArrayList<Location> coords = new ArrayList<>();
        coords.add(new Location(start.getLati(), start.getLongi()));
        counter++;
        for(int distance = 0; distance < dist; distance ++) {
            Location coord = getDestinationLatLng(coords.get(counter).getLati(), coords.get(counter).getLongi(), azimuth, coveredDist);
            coveredDist += interval;
            coords.add(coord);
            azimuth=calculateBearing(coords.get(counter), coords.get(counter+1));
            
        }
        coords.add(new Location(end.getLati(), end.getLongi()));

        return coords;

    }

	public static double getPathLength(Location start, Location end) {
        double lat1Rads = Math.toRadians(start.getLati());
        double lat2Rads = Math.toRadians(end.getLati());
        double deltaLat = Math.toRadians(end.getLati() - start.getLati());

        double deltaLng = Math.toRadians(end.getLongi() - start.getLongi());
        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) + Math.cos(lat1Rads) * Math.cos(lat2Rads) * Math.sin(deltaLng/2) * Math.sin(deltaLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = 6371000 * c;
        return d;
    }
	
	
	 public static Location getDestinationLatLng(double lat, double lng, double azimuth, double distance) {
	        double radiusKm = 6371000 / 1000; //Radius of the Earth in km
	        double brng = Math.toRadians(azimuth); //Bearing is degrees converted to radians.
	        double d = distance / 1000; //Distance m converted to km
	        double lat1 = Math.toRadians(lat); //Current dd lat point converted to radians
	        double lon1 = Math.toRadians(lng); //Current dd long point converted to radians
	        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / radiusKm) + Math.cos(lat1) * Math.sin(d / radiusKm) * Math.cos(brng));
	        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d / radiusKm) * Math.cos(lat1), Math.cos(d / radiusKm) - Math.sin(lat1) * Math.sin(lat2));
	        //convert back to degrees
	        lat2 = Math.toDegrees(lat2);
	        lon2 = Math.toDegrees(lon2);
	        return new Location(lat2, lon2);
	    }
	
	
	
	 /**
	     * calculates the azimuth in degrees from start point to end point");
	     double startLat = Math.toRadians(start.lat);
	     * @param start
	     * @param end
	     * @return
	     */
	    public static double calculateBearing(Location start, Location end) {
	        double startLat = Math.toRadians(start.getLati());
	        double startLong = Math.toRadians(start.getLongi());
	        double endLat = Math.toRadians(end.getLati());
	        double endLong = Math.toRadians(end.getLongi());
	        double dLong = endLong - startLong;
	        double dPhi = Math.log(Math.tan((endLat / 2.0) + (Math.PI / 4.0)) / Math.tan((startLat / 2.0) + (Math.PI / 4.0)));
	        if (Math.abs(dLong) > Math.PI) {
	            if (dLong > 0.0) {
	                dLong = -(2.0 * Math.PI - dLong);
	            } else {
	                dLong = (2.0 * Math.PI + dLong);
	            }
	        }
	        double bearing = (Math.toDegrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;
	        return bearing;
	    }
	
}
