package research.sumit0161.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import research.sumit0161.common.CommonFunctions;
import research.sumit0161.common.CommonVariables;
import research.sumit0161.customthread.InitializeMobileDevice;
import research.sumit0161.entity.CompleteScenario;
import research.sumit0161.entity.Container;
import research.sumit0161.entity.EdgeConfiguration;
import research.sumit0161.entity.EdgeServer;
import research.sumit0161.entity.Location;
import research.sumit0161.entity.Maneuver;
import research.sumit0161.entity.MobileDevice;

//step1 adding edge server locations to the list
//step2 initializing edge servers as per user input with max of list size

public class Launcher {

	public static ArrayList<Location> serverLocations=new ArrayList<Location>();
	
	public static void setupScenario()
	{
				//Step 1
				//adding edge server locations to a list
				serverLocations.add(new Location(30.719036,76.740203));
				serverLocations.add( new Location(30.733055,76.763034));
				serverLocations.add( new Location(30.746924,76.78535));
				
				serverLocations.add(new Location(30.706492,76.751018));
				serverLocations.add(new Location(30.720807,76.773162));
				serverLocations.add(new Location(30.735121,76.796336));
				
				serverLocations.add(new Location(30.695274,76.760974));
				serverLocations.add( new Location(30.709443,76.783634));
				serverLocations.add(new Location(30.724054,76.806636));
				
				
				
				//Step 2
				//taking user input about type of server
				CommonVariables.servers=initialiseEdgeServer();
				
				
				//Step 3
				//Reading possible mobile location from excel
				try {
					CommonVariables.possibleMobileDevices=readMobileDeviceLocations();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Step 4
				//Constructing mobile devices
				constructingUniqueMobileDevices();
				
	
	}
	
	public static void main(String[] args) 
	{
		try(ObjectInputStream ois= new ObjectInputStream(new FileInputStream("D:\\research.data"))) 
		{
			CompleteScenario cs=(CompleteScenario)ois.readObject();
			if(cs!=null)
			{	
				System.out.println("To continue working with scenario fetched from file press y else n");
				if(((new Scanner(System.in)).next().charAt(0)+"").equalsIgnoreCase("n"))
				{
					System.out.println("------------------- adding servers and mobile devices");
					setupScenario();
				}
				else if(cs.getMobileDevices().get(0).getSpeedBasedEverySecondLocation().size()<2)
				{
					System.out.println("------------------- generating locations at all time instances for all devices");
					generateTimeBasedLocationData(cs);
					System.out.println("Time based locations generated");
					System.out.println("------------------- Calculating distances at all time instances for all devices");
					cs.setDistances(calculateDistancesForEachServerOfMobileDevices(cs));
					overWriteCompleteScenarioOnFile(cs);
					
					
				}
				else
				{
					
				
//					cs.getMobileDevices().get(0).getSpeedBasedEverySecondLocation().stream().forEach(arg0->{
//						System.out.println(arg0);
//					});
//					
//					cs.getMobileDevices().stream().forEach(arg0->{
//						System.out.println(arg0.totalDistance+"m is Total Distance ,"+arg0.getBandwidth()+"kbps is bandwidth ,"+arg0.getSpeed()+"m/s is speed ,"+arg0.getCpu()+" is cpu required ,"+arg0.getCurrentLocation()+" is currentLocation");
//					});
//					
//					cs.getServers().stream().forEach(arg0->{
//						System.out.println(arg0.getBandwidth()+"mbps is bandwidth ,"+arg0.getEnergy()+" is energy ,"+arg0.getCPUCapacity()+" is capacity, "+arg0.getCPUUsage()+" is current cpu usage ,"+arg0.getStorage()+" is storage ,"+arg0.getAllContainers().size()+" are number of conainers,"+arg0.getAllContainers().get(0)+" is definition of one container");
//					});
					
					latencyCalculatorAndThenServerAssigner(cs);
					System.out.println(cs);
				}
				
			}
			else
			{
				System.out.println("Object read is null so trying to setup");
				setupScenario();
			}
			ois.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("IOException occured so trying to setup");
			setupScenario();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Test for finding points in between two points
		
//		Location start=new Location(30.7336277,76.7716378);
//		Location end= new Location(30.7327938,76.7722034);
//		System.out.println("Distance for first mobile maneuver is-->"+CommonFunctions.getPathLength(start, end));
//		CommonFunctions.getLocations(10, CommonFunctions.calculateBearing(start, end), start, end).stream().forEach(arg0->System.out.println(arg0));;
		
		
		
			
	
	}
	
	public static void overWriteCompleteScenarioOnFile(CompleteScenario cs)
	{
		try(FileOutputStream output = new FileOutputStream("D:\\research.data", false);ObjectOutputStream ooS= new ObjectOutputStream(output)) 
		{
			ooS.writeObject(cs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void latencyCalculatorAndThenServerAssigner(CompleteScenario cs)
	{
		
		float[][][] distances=cs.getDistances();
//		HashMap<MobileDevice,ArrayList<EdgeServer>>suited=findPossibleServers(cs);
//		Set o=suited.entrySet();
//		Iterator i=o.iterator();
//		while(i.hasNext())
//		{
//			MD,ES,L
		for(int i=0;i<cs.getMobileDevices().size();i++)
		{
			int lowestLatencyServerIndex=0;
			double lowestLatency=Double.POSITIVE_INFINITY;
			TreeMap<Double,Integer> mapOfLatencies= new TreeMap<>();
			
			for(int j=0;j<cs.getServers().size();j++)
			{
				double bandWidth=cs.getMobileDevices().get(i).getBandwidth()<cs.getServers().get(j).getBandwidth()*1000?cs.getMobileDevices().get(i).getBandwidth():cs.getServers().get(j).getBandwidth();
				float distance= distances[0][i][j];
				int packetSize=(CommonFunctions.randomValueBetweenMinAndMax(500, 1500)*8)/1000;  //kilobit
				double serializationDelay=packetSize/bandWidth;
				
				double propagationDelay= distance/300000;
				
				
				double latency=serializationDelay+propagationDelay;
				mapOfLatencies.put(latency, j);
//				if(latency<lowestLatency)
//				{
//					lowestLatency=latency;
//					lowestLatencyServerIndex=j;
//				}
//				System.out.println(""+(i+1)+","+cs.getMobileDevices().get(i).getBandwidth()+","+(j+1)+","+cs.getServers().get(j).getBandwidth()*1000+","+distance+","+latency+","+propagationDelay+","+serializationDelay);
			}
			
			  Set<?> set = mapOfLatencies.entrySet();
		      Iterator<?> iterator = set.iterator();
		      while(iterator.hasNext()) 
		      {
		    	  	Map.Entry mentry = (Map.Entry)iterator.next();
		         	while(assignMobileDeviceToEdgeServer(cs, i, (Integer)mentry.getValue())==Boolean.FALSE)
					{
						
					}
		         	break;
		      }
		      
//			System.out.println(cs.getMobileDevices().get(i));
			
		}
		//}
	}
	
	
	
	
	
	public static void edgeServerContainerListUpdation(CompleteScenario cs)
	{
		cs.getServers().stream().forEach(arg0->{
			
			arg0.getAllContainers().stream().forEach(arg00->{
				if(arg00.getCapacity()>arg00.getThreshold())
				{
					
				}
			});
			
		});
	}
	
	public static boolean assignMobileDeviceToEdgeServer(CompleteScenario cs, int mobileDeviceIndex, int edgeServerIndex)
	{
		MobileDevice tempMd=cs.getMobileDevices().get(mobileDeviceIndex);
		EdgeServer tempEs=cs.getServers().get(edgeServerIndex);
		
		
//		cs.getServers().get(edgeServerIndex).setCPUUsage(newUsage);
		
		ArrayList<Container> overloadedContainers= cs.getServers().get(edgeServerIndex).getAllContainers();
		for (Iterator<Container> iterator = overloadedContainers.iterator(); iterator.hasNext();) 
		{
			Container container = iterator.next();
			
			if(container.setCurrentUsage(tempMd.getCpu()))
			{
				System.out.println("Container is Assigned now");
				cs.getMobileDevices().get(mobileDeviceIndex).setServingEdgeServer(cs.getServers().get(edgeServerIndex));
				return true;
			}
		}
		cs.getServers().get(edgeServerIndex).setAllContainers(overloadedContainers);
		
//		cs.getServers().get(edgeServerIndex).getOverloadedContainers().stream().forEach(arg0->{
//			
//			while(!arg0.setCurrentUsage(tempMd.getCpu()))
//			{
//				
//			}
//		});
		
		return false;
		
	}
	
	public static HashMap<MobileDevice,ArrayList<EdgeServer>> findPossibleServers(CompleteScenario cs)
	{
		HashMap<MobileDevice,ArrayList<EdgeServer>> suited= new HashMap<MobileDevice,ArrayList<EdgeServer>>(); 
		cs.getMobileDevices().parallelStream().forEach(arg0->{
			ArrayList<EdgeServer>temp= new ArrayList<>();
			cs.getServers().parallelStream().forEach(arg00->{
				
				if((arg00.getCPUCapacity()-arg00.getCPUUsage())>arg0.getCpu())
				{
					temp.add(arg00);
				}
				
			});
			suited.put(arg0, temp);
		});
		
		return suited;
	}
	
	public static float[][][] calculateDistancesForEachServerOfMobileDevices(CompleteScenario cs)
	{
		int seconds=1;
		float [][][]distances= new float[seconds][cs.getMobileDevices().size()][cs.getServers().size()];
		for(int k=0;k<seconds;k++)
		{
		for (int i = 0; i < cs.getMobileDevices().size(); i++) {
			
			for (int j = 0; j < cs.getServers().size(); j++) {
					
				double distance=CommonFunctions.getPathLength(cs.getMobileDevices().get(i).getSpeedBasedEverySecondLocation().get(k), cs.getServers().get(j).getServerLocation());
				float d=(float) (distance/1000);
				distances[k][i][j]=d;
				//System.out.println(""+(i+1)+","+k+","+(j+1)+","+ String.format("%.2f", d)+"km");
				}
			}
		}
		return distances;
	}
	
	public static void generateTimeBasedLocationData(CompleteScenario cs)
	{
		System.out.println(cs);
		
		cs.getMobileDevices().parallelStream().forEach(arg0->
		{
			
			int speed=arg0.getSpeed();
			ArrayList<Location> allpoints= new ArrayList<Location>();
			for(int i=0;i<arg0.navigation.size()-1;i++)
			{
				Maneuver[]oo=arg0.getNavigation();
				if(oo!=null)
				allpoints.addAll(CommonFunctions.getLocations(speed, CommonFunctions.calculateBearing(oo[0].getCurrent(), oo[1].getCurrent()), oo[0].getCurrent(), oo[1].getCurrent()));
				System.out.println(allpoints);
			}
			arg0.setSpeedBasedEverySecondLocation(allpoints);
			System.out.println(arg0);
			
			
		});
		
		overWriteCompleteScenarioOnFile(cs);
		
		
		
		
	}
	
	public static void constructingUniqueMobileDevices()
	{
		HashSet<Location> uniqueLocationList= new HashSet<Location>();
		Scanner objectScanner= new Scanner(System.in);
		System.out.println("How many mobile devices will you be adding to the map?");
		int mobileDevices=100;
		Random objectRandom= new Random();
		while(uniqueLocationList.size()!=mobileDevices)
		{
			uniqueLocationList.add(CommonVariables.possibleMobileDevices.get(objectRandom.nextInt(156)));
		}
		
		uniqueLocationList.stream().forEach(arg0->
		{
			//finding location which is not same as starting location for each mobile device
			Location endLocation=CommonVariables.possibleMobileDevices.get(objectRandom.nextInt(156));
			while(arg0.equals(endLocation))
			{
				endLocation=CommonVariables.possibleMobileDevices.get(objectRandom.nextInt(156));
			}
			InitializeMobileDevice runnableForConstructingMobileDevice= new InitializeMobileDevice(arg0, endLocation);
			Thread threadForConstructingMobileDevice= new Thread(runnableForConstructingMobileDevice);
			threadForConstructingMobileDevice.start();
			try {
				threadForConstructingMobileDevice.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			objectScanner.close();
		});
		System.out.println("After all threads");
		CompleteScenario cs= new CompleteScenario();
		cs.setMobileDevices(CommonVariables.mobileDevices);
		cs.setServers(CommonVariables.servers);
		overWriteCompleteScenarioOnFile(cs);
	} 
	
	public static void sendGet() throws Exception {
			
			String url = "https://route.api.here.com/routing/7.2/calculateroute.json?app_id=4uaRTQHpqlbK7GfR7RJk&app_code=euv38POmKa7_UPoJaiSZKw&waypoint0=geo!30.729711,76.757454&waypoint1=geo!30.70602,76.766489&mode=fastest;car;traffic:disabled";
	        String inputLine;
			String exploreJSONCall = url;
			System.out.println(exploreJSONCall);
			URL myURL = new URL(exploreJSONCall);
			HttpURLConnection yc = (HttpURLConnection) myURL.openConnection();
			yc.setRequestMethod("GET");
			yc.setRequestProperty("User-Agent", "Mozilla/5.0");
			yc.setRequestProperty("followAllRedirects", "true");
			yc.setRequestProperty("authority", "route.api.here.com");
			yc.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			System.out.println("Content type expected:"+yc.getContentType());
			int respCode = yc.getResponseCode();
			System.out.println("Response code received:"+Integer.toString(respCode));
			if (respCode != 200) {
			    throw new RuntimeException("Failed : HTTP error code : "+respCode);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
			                        yc.getInputStream()));
			inputLine=in.readLine();
			
			//parsing the directions received from HERE maps api
			JSONParser object= new JSONParser();	
			Object obj = object.parse(inputLine);
	        JSONObject jo = (JSONObject)obj;
	        JSONObject response=(JSONObject)jo.get("response");
	        JSONArray route=(JSONArray)response.get("route");
	        JSONObject zero=(JSONObject)route.get(0);
	        JSONArray leg=(JSONArray)zero.get("leg");
	        JSONObject zero2=(JSONObject)leg.get(0);
	        JSONArray maneuver=(JSONArray)zero2.get("maneuver");
	        for(int i=0;i<maneuver.size()-1;i++)
	        {
	        JSONObject zero3=(JSONObject)maneuver.get(i);
	        JSONObject currentLatLong=(JSONObject)zero3.get("position");
	        System.out.println("lat"+currentLatLong.get("latitude")+"   long"+currentLatLong.get("longitude"));
	        System.out.println(zero3.get("travelTime"));
	        System.out.println(zero3.get("length"));
	        }
	        
	        System.out.println(((JSONObject)zero.get("summary")).get("text"));
	        
	        
	        


		}
	
	public static ArrayList<Location> readMobileDeviceLocations() throws IOException  {
		 	ArrayList<Location> LocationSet=new ArrayList<Location>();
		 	String filePath="D:\\ResearchPaperLocations.xls";
	        File inputWorkbook = new File(filePath);
	        Workbook w;
	        try {
	            w = Workbook.getWorkbook(inputWorkbook);
	            // Get the first sheet
	            Sheet sheet = w.getSheet(0);
	            // Loop over first 10 column and lines

	            for (int j = 0; j < sheet.getColumns(); j++) 
	            {
	                for (int i = 0; i < sheet.getRows(); i++) 
	                {
	                    Cell cell = sheet.getCell(j, i);
	                    CellType type = cell.getType();
	                    if (type == CellType.LABEL) 
	                    {
	                    	String latlong=cell.getContents();
	                    	Location object= new Location(Double.parseDouble(latlong.split(",")[0]),Double.parseDouble(latlong.split(",")[1]));
	                    	LocationSet.add(object);
	                    }
	                }
	            }
	        } catch (BiffException e) {
	            e.printStackTrace();
	        }
	        return LocationSet;
	    }

	public static ArrayList<EdgeServer> initialiseEdgeServer()
		{
			Scanner objectScanner=new Scanner(System.in);
			System.out.println("How many edge servers you want to deploy in area under consideration, max allowed are "+serverLocations.size());
			int numberOfServers=objectScanner.nextInt();
			
			while(numberOfServers>serverLocations.size()||numberOfServers<1)
			{
				System.out.println("This quantity of servers is not allowed");
				System.out.println("Try again,How many edge servers you want to deploy in area under consideration, max allowed are"+serverLocations.size());
				numberOfServers=objectScanner.nextInt();
			}
				
			System.out.println("  ");System.out.println("  ");System.out.println("  ");
			System.out.println("If you want to deploy homogenous Edge Server enter M or If you want to deploy heterogeneous Edge Server press T");
			char typeOfServer=objectScanner.next().charAt(0);
			while(!(typeOfServer+"").equalsIgnoreCase("M")&&!(typeOfServer+"").equalsIgnoreCase("t"))
			{
				System.out.println("The system cannot recognize your input");
				System.out.println("Try Again,If you want to deploy homogenous Edge Server enter M or If you want to deploy heterogeneous Edge Server press T");
				typeOfServer=objectScanner.next().charAt(0);
			}
			objectScanner.close();
			return typeOfServer=='T'||typeOfServer=='t'?Launcher.initializingHeterogeneousEdgeServers(numberOfServers):Launcher.initializingHomogeneousEdgeServers(numberOfServers);
			
		}
		
	public static ArrayList<EdgeServer> initializingHeterogeneousEdgeServers(int numberOfServers)
	{
		ArrayList<EdgeServer> servers= new ArrayList<EdgeServer>();
		Random objectRandom= new Random();
		for(int i=0;i<numberOfServers;i++)
		{
			EdgeConfiguration config=null;
			int configurationSelector=objectRandom.nextInt(12);
			if(configurationSelector<3&&configurationSelector>=0)
			{
				config=CommonVariables.FirstConfiguration;
			}
			else if(configurationSelector<6&&configurationSelector>=3)
			{
				config=CommonVariables.SecondConfiguration;
			}
			else if(configurationSelector<9&&configurationSelector>=6)
			{
				config=CommonVariables.ThirdConfiguration;
			}
			else if(configurationSelector<6&&configurationSelector>=3)
			{
				config=CommonVariables.FourthConfiguration;
			}
			else
			{
				config=CommonVariables.FirstConfiguration;
			}
			EdgeServer object= new EdgeServer();
			object.setServerLocation(serverLocations.get(i));
			object.setConfig(config);
			servers.add(object);
		}
		return servers;
	}
	
	public static ArrayList<EdgeServer> initializingHomogeneousEdgeServers(int numberOfServers)
	{
		ArrayList<EdgeServer> servers= new ArrayList<EdgeServer>();
		Random objectRandom= new Random();
		EdgeConfiguration config=null;
		int configurationSelector=objectRandom.nextInt(12);
		if(configurationSelector<3&&configurationSelector>=0)
		{
			config=CommonVariables.FirstConfiguration;
		}
		else if(configurationSelector<6&&configurationSelector>=3)
		{
			config=CommonVariables.SecondConfiguration;
		}
		else if(configurationSelector<9&&configurationSelector>=6)
		{
			config=CommonVariables.ThirdConfiguration;
		}
		else if(configurationSelector<6&&configurationSelector>=3)
		{
			config=CommonVariables.FourthConfiguration;
		}
		else
		{
			config=CommonVariables.FirstConfiguration;
		}
		for(int i=0;i<numberOfServers;i++)
		{
			
			EdgeServer object= new EdgeServer();
			object.setConfig(config);
			object.setServerLocation(serverLocations.get(i));
			servers.add(object);
			System.out.println(object);
		}
		return servers;
	}
	
	
	
	
	
	
}
