/* Thao Nguyen (501017698)
 *
 * A Long Haul Flight is a flight that travels a long distance and has two types of seats (First Class and Economy)
 */

public class LongHaulFlight extends Flight
{
	int firstClassPassengers;
		
	public LongHaulFlight(String flightNum, String airline, String dest, String departure, int flightDuration, Aircraft aircraft)
	{
		super(flightNum, airline, dest, departure, flightDuration, aircraft);
		type = Flight.Type.LONGHAUL;
	}

	public LongHaulFlight()
	{
		firstClassPassengers = 0;
		type = Flight.Type.LONGHAUL;
	}
	
	public void assignSeat(Passenger p)
	{
		int seat = random.nextInt(aircraft.getNumFirstClassSeats());
		p.setSeat("FCL"+ seat);
	}

	// override getFlightType()
	@Override
	public Type getFlightType()
	{
		return type;
	}

	@Override
	public void reserveSeat(Passenger p, String seat) throws DuplicatePassengerException, SeatOccupiedException, InvalidSeatException
	{
		// checks if seat exists
		if (!super.validSeat(seat)) 
		{
			throw new InvalidSeatException("\nFlight " + super.getFlightNum() + " Invalid Seat Type Request");
		}
		// is first class seat
		if (seat.contains("+"))
		{
			// checks for duplicate seats
			if (!seatMap.isEmpty() && seatMap.containsKey(seat)) 
			{
				throw new SeatOccupiedException("\nSeat " + seat + " is already occupied");
			}
			// checks for duplicate passengers
			if (manifest.indexOf(p) >=  0)
			{
				throw new DuplicatePassengerException("\nDuplicate Passenger " + p.getName() + " " + p.getPassport());
			}
			manifest.add(p);
			seatMap.put(seat, p);
			firstClassPassengers++;
		}
		else // economy passenger
			super.reserveSeat(p, seat);
	}
	@Override
	public void cancelSeat(Passenger p) throws PassengerNotInManifestException
	{
		// first class seats
		if (p.getSeat().contains("+"))
		{
			// checks if passenger is in manifest
			if (manifest.indexOf(p) == -1) 
			{
				throw new PassengerNotInManifestException("Passenger " + p.getName() + " " + p.getPassport() + " Not Found");
			}
			manifest.remove(p);
			String seat = super.removeSeat(p);
			seatMap.remove(seat);

			if (firstClassPassengers > 0)	firstClassPassengers--;
		}
		else // economy passenger
			super.cancelSeat(p);
	}
	
	public int getPassengerCount()
	{
		return getNumPassengers() +  firstClassPassengers;
	}
	
	public String toString()
	{
		 return super.toString() + "\t LongHaul";
	}

}
