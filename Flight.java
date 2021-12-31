import java.util.ArrayList;
import java.util.Random;
// new classes
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

public class Flight
{
	public enum Status {DELAYED, ONTIME, ARRIVED, INFLIGHT};
	public static enum Type {SHORTHAUL, MEDIUMHAUL, LONGHAUL};
	public static enum SeatType {ECONOMY, FIRSTCLASS, BUSINESS};

	private String flightNum;
	private String airline;
	private String origin, dest;
	private String departureTime;
	private Status status;
	private int flightDuration;
	protected Aircraft aircraft;
	protected int numPassengers;
	protected Type type;
	// new fields
	protected ArrayList<Passenger> manifest;
	protected TreeMap<String, Passenger> seatMap;
	
	protected Random random = new Random();

	public Flight()
	{
		this.flightNum = "";
		this.airline = "";
		this.dest = "";
		this.origin = "Toronto";
		this.departureTime = "";
		this.flightDuration = 0;
		this.aircraft = null;
		numPassengers = 0;
		status = Status.ONTIME;
		type = Type.MEDIUMHAUL;
		// new fields
		manifest = new ArrayList<>();
		seatMap = new TreeMap<>();
	}
	
	public Flight(String flightNum)
	{
		this.flightNum = flightNum;
	}
	
	public Flight(String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft)
	{
		this.flightNum = flightNum;
		this.airline = airline;
		this.dest = dest;
		this.origin = "Toronto";
		this.departureTime = departure;
		this.flightDuration = flightDuration;
		this.aircraft = aircraft;
		numPassengers = 0;
		status = Status.ONTIME;
		type = Type.MEDIUMHAUL;
		manifest = new ArrayList<>();
		seatMap = new TreeMap<>();
	}
	
	public Type getFlightType()
	{
		return type;
	}
	public String getFlightNum()
	{
		return flightNum;
	}
	public void setFlightNum(String flightNum)
	{
		this.flightNum = flightNum;
	}
	public String getAirline()
	{
		return airline;
	}
	public void setAirline(String airline)
	{
		this.airline = airline;
	}
	public String getOrigin()
	{
		return origin;
	}
	public void setOrigin(String origin)
	{
		this.origin = origin;
	}
	public String getDest()
	{
		return dest;
	}
	public void setDest(String dest)
	{
		this.dest = dest;
	}
	public String getDepartureTime()
	{
		return departureTime;
	}
	public void setDepartureTime(String departureTime)
	{
		this.departureTime = departureTime;
	}
	public Status getStatus()
	{
		return status;
	}
	public void setStatus(Status status)
	{
		this.status = status;
	}
	public int getFlightDuration()
	{
		return flightDuration;
	}
	public void setFlightDuration(int dur)
	{
		this.flightDuration = dur;
	}
	public int getNumPassengers()
	{
		return numPassengers;
	}
	public void setNumPassengers(int numPassengers)
	{
		this.numPassengers = numPassengers;
	}
	// print seat layout, along with any occupied seats
	public void printSeats()
	{
		String[][] seatLayout = aircraft.getSeatLayout();

		int count = 1;
	
		System.out.println();

		for (int i = 0; i < aircraft.row; i++) {
			for (int j = 0; j < aircraft.column; j++) {

				    String seat = seatLayout[i][j];
					
					if (seatMap.containsKey(seat) && seat.length() == 3) {
						System.out.print("XX  ");
					}
					else if (seatMap.containsKey(seat) && seat.length() == 2) {
						System.out.print("XX ");
					} else {
						System.out.print(seatLayout[i][j] + " ");
					}
			}
			if (count == 2) {
				System.out.println();
			}
				System.out.println();
			count++;
		}
	}
	// cancel seat method
	public void cancelSeat(Passenger p) throws PassengerNotInManifestException
	{
		// check if passenger is in manifest
		if (manifest.indexOf(p) == -1) 
		{
			throw new PassengerNotInManifestException("\nPassenger " + p.getName() + " " + p.getPassport() + " Not Found");
		}
		manifest.remove(p);
		String seat = removeSeat(p);
		seatMap.remove(seat);
		if (numPassengers > 0) numPassengers--;
	}
	// remove seat from seatMap
	public String removeSeat(Passenger p) 
	{
		Set<String> keys = seatMap.keySet();
		String seat = "";

		for (String key : keys) {
			if (seatMap.get(key).equals(p)) {
				seat = key;
			}
		}
		return seat;
	}
	// reserve seat method
	public void reserveSeat(Passenger p, String seat) throws DuplicatePassengerException, SeatOccupiedException, InvalidSeatException
	{
		// check if seat exists
		if (!validSeat(seat)) 
		{
			throw new InvalidSeatException("\nFlight " + flightNum + " Invalid Seat Type Request");
		}
		// check for duplicate seats
		if (!seatMap.isEmpty() && seatMap.containsKey(seat)) {
			throw new SeatOccupiedException("\nSeat " + seat + " is already occupied");
		}
		// Check for duplicate passenger
		if (manifest.indexOf(p) >=  0)
		{
			throw new DuplicatePassengerException("\nDuplicate Passenger " + p.getName() + " " + p.getPassport());
		}
		manifest.add(p);
		seatMap.put(seat, p);
		numPassengers++;
	}
	// check if the seat exists
	public boolean validSeat(String seat) 
	{
		String[][] seatLayout = aircraft.getSeatLayout();

		for (int i = 0; i < aircraft.row; i++) {
			for (int j = 0; j < aircraft.column; j++) {
				if (seatLayout[i][j].equals(seat)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean equals(Object other)
	{
		Flight otherFlight = (Flight) other;
		return this.flightNum.equals(otherFlight.flightNum);
	}
	
	public String toString()
	{
		 return airline + "\t Flight:  " + flightNum + "\t Dest: " + dest + "\t Departing: " + departureTime + "\t Duration: " + flightDuration + "\t Status: " + status;
	}
	// print the passenger manifest
	public void printPassengerManifest()
	{
		System.out.println();
		for (Passenger passenger : manifest) {
			System.out.println(passenger.toString());
		}	
	}
}

// Exception classes

class DuplicatePassengerException extends Exception {

	public DuplicatePassengerException() {}
	public DuplicatePassengerException(String message) {
		super(message);
	}
}
class PassengerNotInManifestException extends Exception {

	public PassengerNotInManifestException() {}
	public PassengerNotInManifestException(String message) {
		super(message);
	}
}
class SeatOccupiedException extends Exception {

	public SeatOccupiedException() {} 
	public SeatOccupiedException(String message) {
		super(message);
	}
}
class InvalidSeatException extends Exception {

	public InvalidSeatException() {}
	public InvalidSeatException(String message) {
		super(message);
	}
}
