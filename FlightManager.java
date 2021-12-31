import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
// new classes
import java.util.TreeMap;
import java.io.File;
import java.util.Set;
import java.io.IOException;

public class FlightManager
{
  TreeMap<String, Flight> flights = new TreeMap<>();
  
  String[] cities 	= 	{"Dallas", "New York", "London", "Paris", "Tokyo"};
  final TreeMap<String, Integer> index = new TreeMap<>();
  
  int[] flightTimes = { 3, // Dallas
  											1, // New York
  											7, // London
  											8, // Paris
  											16,// Tokyo
  										};
  
  ArrayList<Aircraft> airplanes = new ArrayList<>();  
  ArrayList<String> flightNumbers = new ArrayList<>();
  
  Random random = new Random();
  
  public FlightManager() throws IOException
  {
	// move flightTime indexes into a TreeMap
	index.put("DALLAS", 0); index.put("NEWYORK", 1); index.put("LONDON", 2); index.put("PARIS", 3); index.put("TOKYO", 4);
  	
	// Create some aircraft types with max seat capacities
	// change the seat capacities to multiples of 4
  	airplanes.add(new Aircraft(84, "Boeing 737"));
  	airplanes.add(new Aircraft(44,"Airbus 320"));
  	airplanes.add(new Aircraft(20, "Dash-8 100"));
  	airplanes.add(new Aircraft(12, "Bombardier 5000"));
  	airplanes.add(new Aircraft(100, 16, "Boeing 747"));

	String airline = "";
	String dest = "";
	String depart = "";
	int pascap = 0;
	int firstClass = 0;
	Aircraft aircraft = null;
	
	// reading file goes through fileExists() and readFile() methods
	File file = new File("flights.txt");
	fileExists(file);
	Scanner scanner = new Scanner(file);
	readFile(scanner, airline, dest, depart, pascap, firstClass, aircraft);

  }
  // check if file exists
  public void fileExists(File file) throws IOException
  {
	if (!file.exists()) {
		throw new IOException("File Not Found");
	}
  }
  // read the file and generate random flights
  public void readFile(Scanner scanner, String airline, String dest, String depart, int pascap, int firstClass, Aircraft aircraft) throws IOException
  {	
	if (scanner.hasNextLine()) {
		while (scanner.hasNextLine()) {

		String line = scanner.nextLine();
		Scanner lineScanner = new Scanner(line);
		
			while (lineScanner.hasNext()) {
			
				airline = lineScanner.next();
				String[] airlineArr = airline.split("_");
				airline = airlineArr[0] + " " + airlineArr[1];

				dest = lineScanner.next();
				if (dest.contains("_")) {
						String[] destArr = dest.split("_");
						dest = destArr[0] + " " + destArr[1];
					}

				depart = lineScanner.next();
				pascap = lineScanner.nextInt();	

				if (lineScanner.hasNextInt()) {
					firstClass = lineScanner.nextInt();
				} 
			}
			// Populate the list of flights with some random test flights
			String flightNum = generateFlightNumber(airline);
			int flightTime = getFlightTime(dest);
		
			// match flights to aircraft
			if (firstClass == 0) {
				aircraft = matchAircraft(pascap);
			} else {
				aircraft = matchAircraft(pascap, firstClass);
			}

			if (flightTime <= 8) {
				Flight flight = new Flight(flightNum, airline, dest, depart, flightTime, aircraft);
				flights.put(flightNum, flight);
			} else {
				Flight flight = new LongHaulFlight(flightNum, airline, dest, depart, flightTime, aircraft);
				flights.put(flightNum, flight);
			}
		}
	} else {
		throw new IOException("File Data Not Found");
	}
  }

  // match aircraft to flight based on passenger capacity
  private Aircraft matchAircraft(int seats)
  {
	Aircraft minCraft = airplanes.get(0);

	for (Aircraft craft : airplanes) {
		if (seats <= craft.getNumSeats() && craft.getNumSeats() < minCraft.getNumSeats()) {
			minCraft = craft;
		}
	}
	return minCraft;
  }
  // match aircraft to long haul flight based on passenger capacity
  private Aircraft matchAircraft(int seats, int firstClassSeats) 
  {
	for (Aircraft craft : airplanes) {
		if (seats <= craft.getNumSeats() || firstClassSeats <= craft.getNumFirstClassSeats()) {
			return craft;
		}
	}
	return null;
  }
  // get flight duration
  private int getFlightTime(String dest)
  {
	  int i = 0;

	  if (dest.contains(" ")) {
		  String[] city = dest.split(" ");
		  dest = city[0] + city[1];
	  }
	  dest = dest.toUpperCase();
	  
	  Set<String> keySet = index.keySet();
	  for (String key : keySet) {
		if (key.equals(dest)) {
			i = index.get(key);
			i = flightTimes[i];
		}
	  }
	  return i;
  }
  
  private String generateFlightNumber(String airline)
  {
  	String word1, word2;
  	Scanner scanner = new Scanner(airline);
  	word1 = scanner.next();
  	word2 = scanner.next();
  	String letter1 = word1.substring(0, 1);
  	String letter2 = word2.substring(0, 1);
  	letter1.toUpperCase(); letter2.toUpperCase();
  	
  	// Generate random number between 101 and 300
  	boolean duplicate = false;
  	int flight = random.nextInt(200) + 101;
  	String flightNum = letter1 + letter2 + flight;
   	return flightNum;
  }
  
  public void printAllFlights()
  {
	Set<String> keySet = flights.keySet();
  	for (String f : keySet) {

		  Flight flight = flights.get(f);
		  System.out.println(flight);
	  }
  }
  // leads to print seats method
  public void seatsAvailable(String flightNum) throws FlightNotFoundException
  {
    if (!flights.containsKey(flightNum))
  	{
  		throw new FlightNotFoundException("\nFlight " + flightNum + " Not Found");
  	}
   	Flight flight = flights.get(flightNum);
	flight.printSeats();
  }
  
  public Reservation reserveSeatOnFlight(String flightNum, String name, String passport, String seat) throws DuplicatePassengerException, SeatOccupiedException, FlightNotFoundException, InvalidSeatException
  {
  	if (!flights.containsKey(flightNum))
  	{
  		throw new FlightNotFoundException("\nFlight " + flightNum + " Not Found");
  	}
	Flight flight = flights.get(flightNum);

	// if long haul flight
	if (flight.getFlightType().equals(Flight.Type.LONGHAUL))
	{
		LongHaulFlight longFlight = (LongHaulFlight) flight;
		Passenger passenger = new Passenger(name, passport, seat);
		longFlight.reserveSeat(passenger, seat);

    	Reservation res = new Reservation(flightNum, flight.toString(), name, passport, seat);
        return res;
	}
	// for regular flights
	Passenger passenger = new Passenger(name, passport, seat);
	flight.reserveSeat(passenger, seat);
  	return new Reservation(flightNum, flight.toString(), name, passport, seat);
  }
  
  public void cancelReservation(Reservation res) throws PassengerNotInManifestException, FlightNotFoundException
  {
  	if (!flights.containsKey(res.getFlightNum()))
    {
		throw new FlightNotFoundException("\nFlight " + res.getFlightNum() + " Not Found");
	}
	Flight flight = flights.get(res.getFlightNum());
	
	// if long haul flight
	if (flight.getFlightType().equals(Flight.Type.LONGHAUL)) {
		
		LongHaulFlight longFlight = (LongHaulFlight) flight;
		Passenger passenger = new Passenger(res.name, res.passport, res.seat);
		longFlight.cancelSeat(passenger);
	
	} else {
		
		// if regular flight
		Passenger passenger = new Passenger(res.name, res.passport, res.seat);
		flight.cancelSeat(passenger);
	}
  }
  
  public void printAllAircraft()
  {
  	for (Aircraft craft : airplanes)
  		craft.print();
   }

  // print passenger manifest
  public void printManifest(String flightNum) throws FlightNotFoundException
  {
    if (!flights.containsKey(flightNum))
  	{
  		throw new FlightNotFoundException("\nFlight " + flightNum + " Not Found");
  	}
	Flight flight = flights.get(flightNum);
   	flight.printPassengerManifest();
  }
}

// Exception classes

class FlightNotFoundException extends Exception {

	public FlightNotFoundException() {}
	public FlightNotFoundException(String message) {
		super(message);
	}
}
